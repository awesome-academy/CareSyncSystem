package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.CreateBookingRequest;
import com.sun.caresyncsystem.dto.request.RescheduleBookingRequest;
import com.sun.caresyncsystem.dto.response.BookingResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.repository.BookingRepository;
import com.sun.caresyncsystem.repository.ScheduleRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createBooking(Long userId, CreateBookingRequest request) {
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getIsAvailable()) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_AVAILABLE);
        }

        boolean alreadyBooked = bookingRepository.existsByScheduleAndStatus(
                schedule, BookingStatus.CONFIRMED
        );
        if (alreadyBooked) {
            throw new AppException(ErrorCode.SCHEDULE_ALREADY_BOOKED);
        }

        User patient = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        if (patient.getRole() != UserRole.PATIENT) {
            throw new AppException(ErrorCode.ROLE_NOT_ALLOWED);
        }

        Booking booking = new Booking();
        booking.setSchedule(schedule);
        booking.setDoctor(schedule.getDoctor());
        booking.setPatient(patient);
        booking.setAppointmentDate(schedule.getDate());
        booking.setNote(request.note());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        schedule.setIsAvailable(false);
        scheduleRepository.save(schedule);

        bookingRepository.save(booking);
    }

    @Override
    public void rescheduleBooking(Long bookingId, RescheduleBookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new AppException(ErrorCode.INVALID_BOOKING_STATUS);
        }

        Schedule newSchedule = scheduleRepository.findById(request.newScheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!newSchedule.getIsAvailable()) {
            throw new AppException(ErrorCode.SCHEDULE_NOT_AVAILABLE);
        }

        boolean alreadyBooked = bookingRepository.existsByScheduleAndStatus(newSchedule, BookingStatus.CONFIRMED);
        if (alreadyBooked) {
            throw new AppException(ErrorCode.SCHEDULE_ALREADY_BOOKED);
        }

        Schedule oldSchedule = booking.getSchedule();
        oldSchedule.setIsAvailable(true);
        scheduleRepository.save(oldSchedule);

        newSchedule.setIsAvailable(false);
        scheduleRepository.save(newSchedule);

        booking.setSchedule(newSchedule);
        booking.setAppointmentDate(newSchedule.getDate());
        booking.setNote(request.note());
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatus.PENDING &&
                booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new AppException(ErrorCode.INVALID_BOOKING_STATUS);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSchedule().setIsAvailable(true);

        bookingRepository.save(booking);
        scheduleRepository.save(booking.getSchedule());
    }
    @Override
    public Page<BookingResponse> getBookingHistory(Long userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookingPage = bookingRepository.findAllByPatientId(userId, pageable);

        return bookingPage.map(ToDtoMappers::toBookingResponse);
    }
}
