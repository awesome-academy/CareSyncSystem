package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
