package com.sun.caresyncsystem.dto.request;

import com.sun.caresyncsystem.model.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusRequest(
        @NotNull BookingStatus status
) {}
