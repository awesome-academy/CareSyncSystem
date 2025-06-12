package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ApproveDoctorRequest(
        @NotNull Boolean isApproved,
        String rejectReason
) {
}
