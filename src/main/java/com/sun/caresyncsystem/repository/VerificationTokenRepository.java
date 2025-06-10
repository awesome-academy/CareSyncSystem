package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
