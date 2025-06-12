package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
