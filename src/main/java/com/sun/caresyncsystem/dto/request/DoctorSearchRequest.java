package com.sun.caresyncsystem.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record DoctorSearchRequest(
        String name,
        String specialty,
        String service,
        String location,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {}
