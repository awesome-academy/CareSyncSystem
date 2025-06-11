package com.sun.caresyncsystem.model.entity;

import com.sun.caresyncsystem.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String password;
    String fullName;
    String phone;
    String gender;
    String address;
    String avatarUrl;
    @Enumerated(EnumType.STRING)
    UserRole role;
    boolean isActive;
    boolean isVerified;
    boolean isApproved;
    LocalDate dateOfBirth;
    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;
    LocalDateTime deleteAt;
    boolean softDeleted;

    @OneToOne(mappedBy = "user")
    Patient patient;

    @OneToOne(mappedBy = "user")
    Doctor doctor;
}
