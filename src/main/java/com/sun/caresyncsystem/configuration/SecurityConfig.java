package com.sun.caresyncsystem.configuration;

import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.AdminApiPaths;
import com.sun.caresyncsystem.utils.api.DoctorApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.sun.caresyncsystem.utils.api.AuthApiPaths.Endpoint.*;
import static com.sun.caresyncsystem.utils.api.UserApiPaths.Endpoint.FULL_REGISTER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {FULL_REGISTER, FULL_LOGIN, FULL_LOGOUT, FULL_VERIFY_TOKEN, FULL_ACTIVATE,
            FULL_FORGOT_PASSWORD,
            FULL_RESET_PASSWORD,
            DoctorApiPaths.Endpoint.FULL_SEARCH,
            "/chat.html",
            "/ws/chat/**",
            "/favicon.ico"};

    private static final String ROLE = "ROLE_";

    private final MessageUtil messageUtil;

    private final CustomJwtDecoder jwtDecoder;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(messageUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                requests
                        // add end points with not auth here
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        // add end points for admin here
                        .requestMatchers(AdminApiPaths.BASE_ALL)
                        .hasRole(UserRole.ADMIN.name())
                        // Endpoints for doctor
                        .requestMatchers(DoctorApiPaths.BASE_ALL)
                        .hasRole(UserRole.DOCTOR.name())
                        .anyRequest().authenticated());
        http.oauth2ResourceServer(oAuth2 ->
                oAuth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(authenticationEntryPoint()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception -> {
            exception.accessDeniedHandler(accessDeniedHandler());
        });
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint(messageUtil);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ROLE);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
