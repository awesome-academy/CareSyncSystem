package com.sun.caresyncsystem.dto.response;

import lombok.Builder;

@Builder
public record PatientResponse(
        String insuranceNumber,
        String nationalId,
        String medicalHistory
) {}
