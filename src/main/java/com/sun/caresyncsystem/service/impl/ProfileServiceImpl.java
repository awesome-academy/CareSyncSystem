package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.model.entity.Doctor;
import com.sun.caresyncsystem.model.entity.Patient;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.repository.PatientRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public UserResponse getCurrentUserProfile(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND_FROM_TOKEN));

        return switch (currentUser.getRole()) {
            case DOCTOR -> {
                Doctor doctor = doctorRepository.findByUserId(currentUser.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND_FROM_TOKEN));
                yield ToDtoMappers.toUserResponse(currentUser, doctor);
            }
            case PATIENT -> {
                Patient patient = patientRepository.findByUserId(currentUser.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND_FROM_TOKEN));
                yield ToDtoMappers.toUserResponse(currentUser, patient);
            }
            default -> throw new AppException(ErrorCode.UNAUTHORIZED);
        };
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND_FROM_TOKEN));

        updateUserBasicInfo(user, request);
        userRepository.save(user);

        return switch (user.getRole()) {
            case DOCTOR -> {
                Doctor doctor = handleDoctorProfileUpdate(user, request);
                yield ToDtoMappers.toUserResponse(user, doctor);
            }
            case PATIENT -> {
                Patient patient = handlePatientProfileUpdate(user, request);
                yield ToDtoMappers.toUserResponse(user, patient);
            }
            default -> throw new AppException(ErrorCode.UNAUTHORIZED);
        };
    }

    private void updateUserBasicInfo(User user, UpdateProfileRequest request) {
        Optional.ofNullable(request.fullName()).ifPresent(user::setFullName);
        Optional.ofNullable(request.phone()).ifPresent(user::setPhone);
        Optional.ofNullable(request.dateOfBirth()).ifPresent(user::setDateOfBirth);
        Optional.ofNullable(request.gender()).ifPresent(user::setGender);
        Optional.ofNullable(request.address()).ifPresent(user::setAddress);
        Optional.ofNullable(request.avatarUrl()).ifPresent(user::setAvatarUrl);
    }

    private Doctor handleDoctorProfileUpdate(User user, UpdateProfileRequest request) {
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

        applyDoctorUpdates(doctor, request);
        return doctorRepository.save(doctor);
    }

    private void applyDoctorUpdates(Doctor doctor, UpdateProfileRequest request) {
        doctor.setDepartment(request.department());
        doctor.setSpecialization(request.specialization());
        doctor.setBio(request.bio());
    }

    private Patient handlePatientProfileUpdate(User user, UpdateProfileRequest request) {
        Patient patient = patientRepository.findByUserId(user.getId()).orElseGet(() -> {
            Patient newPatient = new Patient();
            newPatient.setUser(user);
            return newPatient;
        });

        applyPatientUpdates(patient, request);
        return patientRepository.save(patient);
    }

    private void applyPatientUpdates(Patient patient, UpdateProfileRequest request) {
        patient.setInsuranceNumber(request.insuranceNumber());
        patient.setNationalId(request.nationalId());
        patient.setMedicalHistory(request.medicalHistory());
    }
}
