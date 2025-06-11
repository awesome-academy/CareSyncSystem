package com.sun.caresyncsystem.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final BaseErrorCode errorCode;
    private final Object[] args;

    public AppException(BaseErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.args = new Object[]{};
    }

    public AppException(BaseErrorCode errorCode, Object... args) {
        super();
        this.errorCode = errorCode;
        this.args = args != null ? args : new Object[]{};
    }
}
