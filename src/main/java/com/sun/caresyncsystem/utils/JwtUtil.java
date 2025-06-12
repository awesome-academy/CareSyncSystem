package com.sun.caresyncsystem.utils;

import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtUtil {
    public static Long extractUserIdFromJwt(Jwt jwt) {
        Long userId = jwt.getClaim(AppConstants.JWT_USER_ID);
        if (userId == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}
