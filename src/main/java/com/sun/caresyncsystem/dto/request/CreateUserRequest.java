package com.sun.caresyncsystem.dto.request;

import com.sun.caresyncsystem.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateUserRequest(
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID")
        String email,
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID_SIZE")
        String password,
        @NotBlank(message = "FULL_NAME_REQUIRED")
        String fullName,
        @NotBlank(message = "PHONE_REQUIRED")
        @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_INVALID_FORMAT")
        String phone,
        @NotBlank(message = "ADDRESS_REQUIRED")
        String address,
        @NotNull(message = "DATE_OF_BIRTH_REQUIRED")
        @Past(message = "DATE_OF_BIRTH_MUST_BE_IN_PAST")
        LocalDateTime dateOfBirth,
        @NotBlank(message = "GENDER_REQUIRED")
        @Pattern(regexp = "MALE|FEMALE|OTHER", message = "GENDER_INVALID")
        String gender,
        String avatarUrl,
        @NotNull(message = "ROLE_REQUIRED")
        UserRole role, // "PATIENT" or "DOCTOR"

        // DOCTOR
        String insuranceNumber,
        String nationalId,
        String medicalHistory,

        // PATIENT
        String department,
        String specialization,
        @Size(max = 1000, message = "BIO_TOO_LONG")
        String bio
) {
}
