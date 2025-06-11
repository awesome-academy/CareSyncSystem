package com.sun.caresyncsystem.dto.request;

import com.sun.caresyncsystem.model.enums.UserRole;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateUserRequest(
        String email,
        @Size(min = 8, message = "PASSWORD_INVALID_SIZE")
        String password,
        String fullName,
        String phone,
        String address,
        LocalDateTime dateOfBirth,
        String gender,
        String avatarUrl,
        UserRole role, // "PATIENT" or "DOCTOR"

        // DOCTOR
        String insuranceNumber,
        String nationalId,
        String medicalHistory,

        // PATIENT
        String department,
        String specialization,
        String bio
) {
}
