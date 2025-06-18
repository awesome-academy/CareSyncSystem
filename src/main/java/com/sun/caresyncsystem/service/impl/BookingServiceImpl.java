package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.repository.BookingRepository;
import com.sun.caresyncsystem.repository.ScheduleRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public void createBooking(Long userId, CreateBookingRequest request) {
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getIsAvailable()) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_AVAILABLE);
        }

        boolean hasBooked = bookingRepository.existsByScheduleAndAppointmentDateAndStatus(
                schedule, request.appointmentDate(), BookingStatus.CONFIRMED);
        if (hasBooked) {
            throw new AppException(ErrorCode.SCHEDULE_ALREADY_BOOKED);
        }

        User patient = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        if (patient.getRole() != UserRole.PATIENT) {
            throw new AppException(ErrorCode.ROLE_NOT_ALLOWED);
        }

        Booking booking = new Booking();
        booking.setSchedule(schedule);
        booking.setPatient(patient);
        booking.setDoctor(schedule.getDoctor());
        booking.setAppointmentDate(request.appointmentDate());
        booking.setNote(request.note());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }
}
