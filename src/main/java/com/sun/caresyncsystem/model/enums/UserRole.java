package com.sun.caresyncsystem.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN,
    DOCTOR,
    PATIENT;

    @JsonCreator
    public static UserRole fromString(String value) {
        try {
            return UserRole.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_USER_ROLE);
        }
    }
}
