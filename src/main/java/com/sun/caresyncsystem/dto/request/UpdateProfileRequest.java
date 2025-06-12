package com.sun.caresyncsystem.dto.request;

import java.time.LocalDate;

public record UpdateProfileRequest(
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String avatarUrl,

        // Doctor
        String department,
        String specialization,
        String bio,

        // Patient
        String insuranceNumber,
        String nationalId,
        String medicalHistory
) {}
