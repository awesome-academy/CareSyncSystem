package com.sun.caresyncsystem.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.BaseErrorCode;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.AccessLog;
import com.sun.caresyncsystem.service.AccessLogService;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.MessageUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements Filter {

    /**
     * Whitelisted IPs allowed to access the app.
     * - Useful for development/testing: restrict access to local/dev machines.
     * - For production: extend this list with trusted IPs (e.g., load balancers, VPNs),
     * or disable the check and handle IP filtering at infrastructure level.
     * To enable: uncomment the IP blocking check below.
     * To disable: comment/remove the check.
     */
    private static final Set<String> ALLOWED_IPS = Set.of(
            "127.0.0.1",
            "0:0:0:0:0:0:0:1",
            "::1",
            "localhost"
    );

    private final AccessLogService accessLogService;
    private final MessageUtil messageUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ip = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");

        long startTime = System.currentTimeMillis();
        try {
            if (!ALLOWED_IPS.contains(ip)) {
                log.warn("[AccessBlock] Blocked suspicious IP: {}", ip);
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            if (userAgent == null || userAgent.isBlank() || userAgent.toLowerCase().contains("bot")) {
                log.warn("[AccessBlock] Blocked suspicious User-Agent: {}", userAgent);
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            chain.doFilter(request, response);
        } catch (AppException e) {
            log.error("[AccessLog] AppException caught in filter: {}", e.getMessage(), e);
            handleException((HttpServletResponse) response, e);
        }

        long duration = System.currentTimeMillis() - startTime;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        String userRole = null;

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt jwt) {
            try {
                userId = JwtUtil.extractUserIdFromJwt(jwt);
            } catch (AppException e) {
                log.warn("[ACCESS LOG] Jwt does not contain userId claim: {}", e.getMessage());
            }
            userRole = auth.getAuthorities().stream().findFirst()
                    .map(Object::toString)
                    .orElse("UNKNOWN");
        }

        AccessLog accessLog = AccessLog.builder()
                .userId(userId)
                .userRole(userRole)
                .method(req.getMethod())
                .uri(req.getRequestURI())
                .ipAddress(ip)
                .userAgent(userAgent)
                .httpStatus(res.getStatus())
                .durationMs(duration)
                .timestamp(LocalDateTime.now())
                .build();

        accessLogService.save(accessLog);

        log.info("[ACCESS LOG] userId={} role={} method={} uri={} status={} ip={} duration={}ms",
                userId, userRole, req.getMethod(), req.getRequestURI(), res.getStatus(), ip, duration);
    }

    private void handleException(HttpServletResponse response, AppException e) throws IOException {
        response.setStatus(e.getErrorCode().getCode());
        response.setContentType("application/json");
        BaseApiResponse<Void> errorResponse = buildErrorResponse(e.getErrorCode(), e.getArgs());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    private BaseApiResponse<Void> buildErrorResponse(BaseErrorCode errorCode, Object... args) {
        BaseApiResponse<Void> response = new BaseApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(messageUtil.getMessage(errorCode.getMessageKey(), args));
        return response;
    }
}
