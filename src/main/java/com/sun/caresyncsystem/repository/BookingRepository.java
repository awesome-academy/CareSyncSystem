package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Booking;
import com.sun.caresyncsystem.model.entity.Schedule;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByScheduleAndStatus(Schedule schedule, BookingStatus status);

    Page<Booking> findAllByPatientId(Long patientId, Pageable pageable);
}
