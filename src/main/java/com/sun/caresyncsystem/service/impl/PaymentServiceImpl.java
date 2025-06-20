package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.CreateVNPayPaymentRequest;
import com.sun.caresyncsystem.dto.response.PaymentResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Payment;
import com.sun.caresyncsystem.model.entity.VNPayPayment;
import com.sun.caresyncsystem.model.enums.PaymentMethod;
import com.sun.caresyncsystem.model.enums.PaymentStatus;
import com.sun.caresyncsystem.repository.BookingRepository;
import com.sun.caresyncsystem.repository.PaymentRepository;
import com.sun.caresyncsystem.service.PaymentService;
import com.sun.caresyncsystem.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.sun.caresyncsystem.utils.VNPayUtil.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final VNPayUtil vnPayUtil;

    @Override
    public PaymentResponse createVNPayPaymentUrl(CreateVNPayPaymentRequest request, HttpServletRequest httpServletRequest) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        String transactionId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.amount())
                .paymentMethod(PaymentMethod.VNPay)
                .status(PaymentStatus.PENDING)
                .transactionId(transactionId)
                .build();
        paymentRepository.save(payment);

        return ToDtoMappers.toPaymentResponse(
                payment,
                vnPayUtil.generatePaymentUrl(transactionId, request.amount(), httpServletRequest)
        );
    }

    @Override
    public PaymentResponse handleVNPayReturn(Map<String, String> vnpParams) {
        boolean isValidSignature = vnPayUtil.verifyReturnDataSignature(vnpParams);
        if (!isValidSignature) {
            throw new AppException(ErrorCode.INVALID_VNPAY_SIGNATURE);
        }

        String transactionId = vnpParams.get(VNP_TRANSACTION_ID_KEY);
        String resultCode = vnpParams.get(VNP_RESPONSE_CODE_KEY);

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (PAYMENT_VNPAY_CODE_SUCCESS.equals(resultCode)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            VNPayPayment vnpDetail = VNPayPayment.builder()
                    .payment(payment)
                    .vnpTransactionNo(vnpParams.get(VNP_TRANSACTION_NO_KEY))
                    .vnpBankCode(vnpParams.get(VNP_BANK_CODE_KEY))
                    .vnpCardType(vnpParams.get(VNP_CARD_TYPE_KEY))
                    .vnpPayDate(vnpParams.get(VNP_PAY_DATE_KEY))
                    .build();

            payment.setVnpayPayment(vnpDetail);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);

        return ToDtoMappers.toPaymentResponse(payment);
    }
}
