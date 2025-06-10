package com.sun.caresyncsystem.dto.response;

import lombok.Builder;

@Builder
public record DoctorResponse(
        String department,
        String specialization,
        String bio,
        Float ratingAvg
) {}
