package com.sun.caresyncsystem.dto.response;

import java.math.BigDecimal;

public record DoctorSearchResponse(
        Long id,
        String fullName,
        String specialization,
        String department,
        String bio,
        Float ratingAvg,
        String location,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean hasAvailableSchedule
) {}
