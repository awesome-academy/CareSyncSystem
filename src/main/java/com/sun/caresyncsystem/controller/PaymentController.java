package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.request.CreateVNPayPaymentRequest;
import com.sun.caresyncsystem.dto.request.RefundPaymentRequest;
import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.dto.response.PaymentResponse;
import com.sun.caresyncsystem.service.PaymentService;
import com.sun.caresyncsystem.utils.MessageUtil;
import com.sun.caresyncsystem.utils.api.PaymentApiPaths;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentApiPaths.BASE)
public class PaymentController {

    private final PaymentService paymentService;
    private final MessageUtil messageUtil;

    @PostMapping(PaymentApiPaths.Endpoint.CREATE_VNPAY)
    public ResponseEntity<BaseApiResponse<PaymentResponse>> createVNPayPayment(
            @RequestBody @Valid CreateVNPayPaymentRequest request,
            HttpServletRequest httpServletRequest
    ) {

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                paymentService.createVNPayPaymentUrl(request, httpServletRequest)
        ));
    }

    @GetMapping(PaymentApiPaths.Endpoint.VNPAY_RETURN)
    public ResponseEntity<BaseApiResponse<PaymentResponse>> handleVNPayReturn(
            @RequestParam Map<String, String> vnpParams
    ) {

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                paymentService.handleVNPayReturn(vnpParams)
        ));
    }

    @PostMapping(PaymentApiPaths.Endpoint.VNPAY_REFUND)
    public ResponseEntity<BaseApiResponse<Void>> refundVNPayPayment(
            @RequestBody RefundPaymentRequest request
    ) {
        paymentService.refundVNPayById(request);

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                messageUtil.getMessage("refund.success")
        ));
    }
}
