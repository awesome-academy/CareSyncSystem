package com.sun.caresyncsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.uncategorized"),
    INVALID_ERROR_KEY(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.invalid.key"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "error.access.denied"),
    USER_EXISTED(HttpStatus.BAD_REQUEST.value(), "error.user.existed"),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "error.user.not.exist"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), "error.login.failed"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "error.invalid.token"),
    CAN_NOT_CREATE_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.can.not.create.token"),
    USER_NOT_FOUND_FROM_TOKEN(HttpStatus.BAD_REQUEST.value(), "error.user.not.found.from.token"),
    ROLE_NOT_ALLOWED(HttpStatus.BAD_REQUEST.value(), "error.role.not.allowed"),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST.value(), "error.invalid.user.role"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "error.unauthorized"),
    INVALID_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED.value(), "error.invalid.verification.token"),
    DOCTOR_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "error.doctor.profile.not.found"),
    DOCTOR_ALREADY_APPROVED(HttpStatus.BAD_REQUEST.value(), "error.doctor.already.approved"),
    DOCTOR_ALREADY_REJECTED(HttpStatus.BAD_REQUEST.value(), "error.doctor.already.rejected"),
    FAILED_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.failed.to.send.email"),
    USER_NOT_VERIFIED(HttpStatus.BAD_REQUEST.value(), "error.user.not.verified"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST.value(), "auth.password.reset.token.expired"),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "error.schedule.not.found"),
    SCHEDULE_ALREADY_BOOKED(HttpStatus.BAD_REQUEST.value(), "error.schedule.already.booked"),
    SCHEDULE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST.value(), "error.schedule.not.available"),
    SCHEDULE_TIME_CONFLICT(HttpStatus.CONFLICT.value(), "error.schedule.time.conflict"),
    SCHEDULE_INVALID_DATE(HttpStatus.BAD_REQUEST.value(), "error.schedule.invalid.date"),
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "booking.not.found"),
    INVALID_BOOKING_STATUS(HttpStatus.BAD_REQUEST.value(), "invalid.booking.status"),
    ACCOUNT_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST.value(), "error.account.already.active"),
    ACCOUNT_ALREADY_DEACTIVATE(HttpStatus.BAD_REQUEST.value(), "error.account.already.deactivated"),
    SCHEDULE_IMPORT_FAIL(HttpStatus.BAD_REQUEST.value(), "schedule.import.fail"),
    IMPORT_FILE_EMPTY(HttpStatus.BAD_REQUEST.value(), "import.file.empty"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "payment.not.found"),
    GENERATE_PAYMENT_URL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.payment.generate.url"),
    INVALID_VNPAY_SIGNATURE(HttpStatus.BAD_REQUEST.value(), "error.invalid.vnpay.signature"),
    REFUND_NOT_ALLOWED(HttpStatus.BAD_REQUEST.value(), "refund.not.allowed"),
    REFUND_FAILED(HttpStatus.BAD_REQUEST.value(), "refund.failed"),
    VNPAY_DETAIL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "VnPay.detail.not.found"),
    ;

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
