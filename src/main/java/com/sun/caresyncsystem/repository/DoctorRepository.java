package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.dto.response.DoctorSearchResponse;
import com.sun.caresyncsystem.model.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);

    Page<Doctor> findByUserIsApprovedFalseAndUserDeletedAtIsNull(Pageable pageable);

    @Query("""
        SELECT new com.sun.caresyncsystem.dto.response.DoctorSearchResponse(
            d.id,
            d.user.fullName,
            d.specialization,
            d.department,
            d.bio,
            d.ratingAvg,
            d.location,
            MIN(s.price),
            MAX(s.price),
            CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        )
        FROM Doctor d
        JOIN d.schedules s
        WHERE d.user.deletedAt IS NULL
        AND (:name IS NULL OR LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:specialty IS NULL OR d.specialization = :specialty)
        AND (:service IS NULL OR d.service = :service)
        AND (:location IS NULL OR d.location = :location)
        AND (:date IS NULL OR s.date = :date)
        AND (:startTime IS NULL OR s.startTime <= :startTime)
        AND (:endTime IS NULL OR s.endTime >= :endTime)
        AND s.isAvailable = true
        GROUP BY d
    """)
    Page<DoctorSearchResponse> searchDoctors(
            @Param("name") String name,
            @Param("specialty") String specialty,
            @Param("service") String service,
            @Param("location") String location,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable
    );
}
