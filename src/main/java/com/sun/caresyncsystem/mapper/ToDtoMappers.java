package com.sun.caresyncsystem.mapper;

import com.sun.caresyncsystem.dto.response.DoctorResponse;
import com.sun.caresyncsystem.dto.response.PatientResponse;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.model.entity.Doctor;
import com.sun.caresyncsystem.model.entity.Patient;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.enums.UserRole;

public class ToDtoMappers {

    public static UserResponse toUserResponse(User user, Patient patient) {

        PatientResponse patientResponse = PatientResponse.builder()
                    .insuranceNumber(patient.getInsuranceNumber())
                    .nationalId(patient.getNationalId())
                    .medicalHistory(patient.getMedicalHistory())
                    .build();

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .isActive(user.isActive())
                .isVerified(user.isVerified())
                .isApproved(user.isApproved())
                .patient(patientResponse)
                .build();
    }

    public static UserResponse toUserResponse(User user, Doctor doctor) {
        DoctorResponse doctorResponse = DoctorResponse.builder()
                    .department(doctor.getDepartment())
                    .specialization(doctor.getSpecialization())
                    .bio(doctor.getBio())
                    .ratingAvg(doctor.getRatingAvg())
                    .build();

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .isActive(user.isActive())
                .isVerified(user.isVerified())
                .isApproved(user.isApproved())
                .doctor(doctorResponse)
                .build();
    }
}
