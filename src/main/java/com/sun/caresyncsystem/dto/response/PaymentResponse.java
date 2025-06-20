package com.sun.caresyncsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.caresyncsystem.model.enums.PaymentMethod;
import com.sun.caresyncsystem.model.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        Long id,
        Long bookingId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        String transactionId,
        LocalDateTime paidAt,
        String paymentUrl
) {}
