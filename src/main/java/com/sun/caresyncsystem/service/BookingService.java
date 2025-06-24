package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.dto.request.RescheduleBookingRequest;

public interface BookingService {
    void createBooking(Long userId, CreateBookingRequest request);

    void rescheduleBooking(Long bookingId, RescheduleBookingRequest request);

    void cancelBooking(Long bookingId);
}
