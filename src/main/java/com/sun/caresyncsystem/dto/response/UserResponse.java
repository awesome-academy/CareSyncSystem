package com.sun.caresyncsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        String gender,
        String address,
        String avatarUrl,
        LocalDateTime dateOfBirth,
        boolean isActive,
        boolean isVerified,
        boolean isApproved,
        DoctorResponse doctor,
        PatientResponse patient
) {
    @Builder
    public record PatientInfo(String insuranceNumber, String nationalId, String medicalHistory) {}

    @Builder
    public record DoctorInfo(String department, String specialization, String bio, Float ratingAvg) {}
}
