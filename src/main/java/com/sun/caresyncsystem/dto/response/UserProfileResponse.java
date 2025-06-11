package com.sun.caresyncsystem.dto.response;

import com.sun.caresyncsystem.model.enums.UserRole;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserProfileResponse(
        String email,
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String avatarUrl,
        UserRole role,

        // Doctor
        String department,
        String specialization,
        String bio,

        // Patient
        String insuranceNumber,
        String nationalId,
        String medicalHistory
) {}
