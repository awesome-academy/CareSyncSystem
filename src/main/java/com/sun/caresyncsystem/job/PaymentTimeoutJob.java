package com.sun.caresyncsystem.job;

import com.sun.caresyncsystem.model.entity.Payment;
import com.sun.caresyncsystem.model.enums.PaymentStatus;
import com.sun.caresyncsystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentTimeoutJob {

    private final PaymentRepository paymentRepository;

    /**
     * Run every 15 minutes
     */
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void markExpiredPayments() {
        log.info("[CLEANUP] Start check payment expired...");
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(15);

        List<Payment> expiredPayments = paymentRepository
                .findByStatusAndCreatedAtBefore(PaymentStatus.PENDING, timeoutThreshold);

        for (Payment payment : expiredPayments) {
            payment.setStatus(PaymentStatus.EXPIRED);
        }

        paymentRepository.saveAll(expiredPayments);
        log.info("[Expired] Done mark expired payments: {}", expiredPayments.size());

    }
}
