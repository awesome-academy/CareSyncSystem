package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;

public interface BookingService {
    void createBooking(Long userId, CreateBookingRequest request);
}
