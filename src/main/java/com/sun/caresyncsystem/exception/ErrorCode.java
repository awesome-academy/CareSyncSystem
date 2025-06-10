package com.sun.caresyncsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.uncategorized"),
    INVALID_ERROR_KEY(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.invalid.key"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "error.access.denied"),
    USER_EXISTED(HttpStatus.BAD_REQUEST.value(), "error.user.existed"),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "error.user.not.exist"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), "error.login.failed"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "error.invalid.token"),
    CAN_NOT_CREATE_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.can.not.create.token"),
    USER_NOT_FOUND_FROM_TOKEN(HttpStatus.BAD_REQUEST.value(), "error.user.not.found.from.token"),
    PASSWORD_INVALID_SIZE(HttpStatus.BAD_REQUEST.value(), "error.password.size"),
    ROLE_NOT_ALLOWED(HttpStatus.BAD_REQUEST.value(), "error.role.not.allowed"),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST.value(), "error.invalid.user.role"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "error.unauthorized"),
    ;

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
