package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.DoctorSearchRequest;
import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    Page<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request, Pageable pageable);

    void updateBookingStatus(Long doctorId, Long bookingId, BookingStatus status);
}
