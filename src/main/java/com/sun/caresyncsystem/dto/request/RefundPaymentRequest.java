package com.sun.caresyncsystem.dto.request;

public record RefundPaymentRequest(
        Long paymentId,
        String actor,
        String ipAddress
) {}
