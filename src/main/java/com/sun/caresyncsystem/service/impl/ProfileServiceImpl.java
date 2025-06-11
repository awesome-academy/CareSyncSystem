package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserProfileResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.model.entity.Doctor;
import com.sun.caresyncsystem.model.entity.Patient;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.repository.PatientRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.AuthenticationService;
import com.sun.caresyncsystem.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AuthenticationService authenticationService;

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User currentUser = getAuthenticatedUser();

        Doctor doctor = doctorRepository.findByUserId(currentUser.getId()).orElse(null);
        Patient patient = patientRepository.findByUserId(currentUser.getId()).orElse(null);

        return ToDtoMappers.toUserProfileResponse(currentUser, doctor, patient);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getAuthenticatedUser();

        updateUserBasicInfo(user, request);
        userRepository.save(user);

        Doctor doctor = null;
        Patient patient = null;

        switch (user.getRole()) {
            case DOCTOR -> doctor = handleDoctorProfileUpdate(user, request);
            case PATIENT -> patient = handlePatientProfileUpdate(user, request);
            default -> throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return ToDtoMappers.toUserProfileResponse(user, doctor, patient);
    }

    private User getAuthenticatedUser() {
        return authenticationService.getCurrentUser();
    }

    private void updateUserBasicInfo(User user, UpdateProfileRequest request) {
        if (request.fullName() != null) user.setFullName(request.fullName());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());
        if (request.gender() != null) user.setGender(request.gender());
        if (request.address() != null) user.setAddress(request.address());
        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());
    }

    private Doctor handleDoctorProfileUpdate(User user, UpdateProfileRequest request) {
        Doctor doctor = doctorRepository.findByUserId(user.getId()).orElseGet(() -> {
            Doctor newDoctor = new Doctor();
            newDoctor.setUser(user);
            return newDoctor;
        });

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
