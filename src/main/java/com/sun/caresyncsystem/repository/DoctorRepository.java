package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    Page<Doctor> findByUserIsApprovedFalseAndUserDeleteAtIsNull(Pageable pageable);
}
