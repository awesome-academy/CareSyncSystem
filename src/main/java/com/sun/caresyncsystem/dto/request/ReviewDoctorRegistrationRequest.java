package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReviewDoctorRegistrationRequest(
        @NotNull Boolean isApproved,
        String rejectReason
) {
}
