package com.sun.caresyncsystem.mapper;

import com.sun.caresyncsystem.dto.response.DoctorResponse;
import com.sun.caresyncsystem.dto.response.PatientResponse;
import com.sun.caresyncsystem.dto.response.PaymentResponse;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.model.entity.*;

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

    public static PaymentResponse toPaymentResponse(Payment payment, String paymentUrl) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .paidAt(payment.getPaidAt())
                .paymentUrl(paymentUrl)
                .build();
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return toPaymentResponse(payment, null);
    }
}
