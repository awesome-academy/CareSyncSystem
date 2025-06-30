package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.dto.request.RescheduleBookingRequest;
import com.sun.caresyncsystem.dto.response.BookingResponse;
import org.springframework.data.domain.Page;

public interface BookingService {
    void createBooking(Long userId, CreateBookingRequest request);

    void rescheduleBooking(Long bookingId, RescheduleBookingRequest request);

    void cancelBooking(Long bookingId);

    Page<BookingResponse> getBookingHistory(Long userId, int page, int size);
}
