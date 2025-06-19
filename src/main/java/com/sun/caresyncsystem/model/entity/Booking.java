package com.sun.caresyncsystem.model.entity;

import com.sun.caresyncsystem.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String note;

    private LocalDateTime createdAt;
}
