package com.sun.caresyncsystem.dto.request;

import java.math.BigDecimal;

public record CreateVNPayPaymentRequest(
        Long bookingId,
        BigDecimal amount
) {
}
