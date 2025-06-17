package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByScheduleAndAppointmentDateAndStatus(Schedule schedule, LocalDate appointmentDate, BookingStatus status);
}
