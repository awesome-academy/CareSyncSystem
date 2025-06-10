package com.sun.caresyncsystem.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(999, "error.uncategorized"),
    INVALID_ERROR_KEY(998, "error.invalid.key"),
    ACCESS_DENIED(403, "error.access.denied"),
    ;

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
