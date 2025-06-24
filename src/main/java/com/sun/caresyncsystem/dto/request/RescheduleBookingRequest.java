package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record RescheduleBookingRequest(
        @NotNull()
        Long newScheduleId,

        String note
) {}
