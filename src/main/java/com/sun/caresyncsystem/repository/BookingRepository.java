package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByScheduleAndStatus(Schedule schedule, BookingStatus status);
}
