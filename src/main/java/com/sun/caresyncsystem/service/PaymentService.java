package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateVNPayPaymentRequest;
import com.sun.caresyncsystem.dto.request.RefundPaymentRequest;
import com.sun.caresyncsystem.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface PaymentService {
    PaymentResponse createVNPayPaymentUrl(CreateVNPayPaymentRequest request, HttpServletRequest httpServletRequest);
    PaymentResponse handleVNPayReturn(Map<String, String> vnpParams);
    void refundVNPayById(RefundPaymentRequest request);
}
