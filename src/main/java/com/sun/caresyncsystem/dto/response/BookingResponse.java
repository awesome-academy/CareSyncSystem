package com.sun.caresyncsystem.dto.response;

import com.sun.caresyncsystem.model.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        Long id,
        String doctorName,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        BookingStatus status,
        String note
) {}
