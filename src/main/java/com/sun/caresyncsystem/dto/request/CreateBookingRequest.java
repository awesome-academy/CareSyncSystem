package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequest(
        @NotNull Long scheduleId,
        @NotNull LocalDate appointmentDate,
        String note
) {}
