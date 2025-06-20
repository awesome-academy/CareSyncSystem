package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.DoctorSearchRequest;
import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import com.sun.caresyncsystem.repository.BookingRepository;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Page<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request, Pageable pageable) {
        return doctorRepository.searchDoctors(
                request.name(),
                request.specialty(),
                request.service(),
                request.location(),
                request.date(),
                request.startTime(),
                request.endTime(),
                pageable
        );
    }

    @Override
    public void updateBookingStatus(Long doctorId, Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getDoctor().getId().equals(doctorId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new AppException(ErrorCode.INVALID_BOOKING_STATUS);
        }

        if (status != BookingStatus.CONFIRMED && status != BookingStatus.REJECTED) {
            throw new AppException(ErrorCode.INVALID_BOOKING_STATUS);
        }

        booking.setStatus(status);
        bookingRepository.save(booking);
    }
}
