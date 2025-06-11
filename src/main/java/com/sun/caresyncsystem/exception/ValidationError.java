package com.sun.caresyncsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ValidationError implements BaseErrorCode {
    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.email.required"),
    EMAIL_INVALID(HttpStatus.BAD_REQUEST.value(), "error.email.invalid"),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.password.required"),
    PASSWORD_INVALID_SIZE(HttpStatus.BAD_REQUEST.value(), "error.password.size"),
    FULL_NAME_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.fullname.required"),
    PHONE_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.phone.required"),
    PHONE_INVALID_FORMAT(HttpStatus.BAD_REQUEST.value(), "error.phone.invalid"),
    ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.address.required"),
    DATE_OF_BIRTH_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.dob.required"),
    DATE_OF_BIRTH_INVALID(HttpStatus.BAD_REQUEST.value(), "error.dob.invalid"),
    GENDER_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.gender.required"),
    GENDER_INVALID(HttpStatus.BAD_REQUEST.value(), "error.gender.invalid"),
    ROLE_INVALID(HttpStatus.BAD_REQUEST.value(), "error.role.invalid"),
    ROLE_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.role.required"),
    BIO_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "error.bio.too.long"),
    PATIENT_INFO_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.patient.info.required"),
    DOCTOR_INFO_REQUIRED(HttpStatus.BAD_REQUEST.value(), "error.doctor.info.required")
    ;

    private final int code;
    private final String messageKey;

    ValidationError(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
