package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Payment;
import com.sun.caresyncsystem.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime time);
}
