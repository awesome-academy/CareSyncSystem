package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBookingRequest(
        @NotNull Long scheduleId,
        String note
) {}
