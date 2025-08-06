package com.sun.caresyncsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String userRole;

    private String method;

    private String uri;

    private String ipAddress;

    private int httpStatus;

    private String userAgent;

    private Long durationMs;

    private LocalDateTime timestamp;
}
