package com.sun.caresyncsystem.dto.request;

import java.math.BigDecimal;

public record VNPayRefundRequest(
        String transactionId,
        int transactionNo,
        String transactionDate,
        BigDecimal amount,
        String transactionType,
        String createBy,
        String ipAddress,
        String orderInfo
) {}
