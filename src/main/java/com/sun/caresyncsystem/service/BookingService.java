package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.model.enums.BookingStatus;

public interface BookingService {
    void createBooking(Long userId, CreateBookingRequest request);

    void updateBookingStatus(Long doctorId, Long bookingId, BookingStatus status);
}
