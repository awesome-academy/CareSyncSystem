package com.sun.caresyncsystem.dto.request;

import com.sun.caresyncsystem.model.enums.Weekday;

import java.time.LocalTime;

public record DoctorSearchRequest(
        String name,
        String specialty,
        String service,
        String location,
        Weekday weekday,
        LocalTime startTime,
        LocalTime endTime
) {}
