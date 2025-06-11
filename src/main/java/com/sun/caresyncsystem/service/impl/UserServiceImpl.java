package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.configuration.AppProperties;
import com.sun.caresyncsystem.dto.request.CreateUserRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.exception.ValidationError;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.model.entity.Doctor;
import com.sun.caresyncsystem.model.entity.Patient;
import com.sun.caresyncsystem.model.entity.User;
import com.sun.caresyncsystem.model.entity.VerificationToken;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.repository.PatientRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.repository.VerificationTokenRepository;
import com.sun.caresyncsystem.service.EmailService;
import com.sun.caresyncsystem.service.PasswordService;
import com.sun.caresyncsystem.service.UserService;
import com.sun.caresyncsystem.utils.api.AuthApiPaths;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;
    private final AppProperties appProperties;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsUserByEmail(request.email()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .gender(request.gender())
                .avatarUrl(request.avatarUrl())
                .dateOfBirth(request.dateOfBirth())
                .password(passwordService.encodePassword(request.password()))
                .isVerified(false)
                .isActive(false)
                .role(request.role())
                .build();

        User savedUser = userRepository.save(user);
        switch (request.role()) {
            case PATIENT -> {
                validatePatientInfo(request);

                Patient patient = Patient.builder()
                        .user(savedUser)
                        .insuranceNumber(request.insuranceNumber())
                        .nationalId(request.nationalId())
                        .medicalHistory(request.medicalHistory())
                        .build();
                patientRepository.save(patient);

                String token = UUID.randomUUID().toString();
                VerificationToken verificationToken = VerificationToken.builder()
                        .token(token)
                        .user(savedUser)
                        .expiryDate(LocalDateTime.now().plusHours(1))
                        .build();
                tokenRepository.save(verificationToken);

                String activationLink = UriComponentsBuilder
                        .fromUriString(appProperties.getBaseUrl())
                        .path(AuthApiPaths.Endpoint.FULL_ACTIVATE)
                        .queryParam("token", token)
                        .build()
                        .toUriString();

                emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getFullName(), activationLink);

                return ToDtoMappers.toUserResponse(savedUser, patient);
            }

            case DOCTOR -> {
                validateDoctorInfo(request);
                savedUser.setApproved(false);
                Doctor doctor = Doctor.builder()
                        .user(savedUser)
                        .department(request.department())
                        .specialization(request.specialization())
                        .bio(request.bio())
                        .ratingAvg(0.0f)
                        .build();
                savedUser.setDoctor(doctor);

                doctorRepository.save(doctor);

                emailService.sendPendingApprovalEmail(
                        savedUser.getEmail(),
                        savedUser.getFullName()
                );

                return ToDtoMappers.toUserResponse(savedUser, doctor);
            }

            default -> throw new AppException(ErrorCode.ROLE_NOT_ALLOWED);
        }
    }

    private void validatePatientInfo(CreateUserRequest request) {
        if (!StringUtils.hasText(request.insuranceNumber()) ||
                !StringUtils.hasText(request.nationalId())) {
            throw new AppException(ValidationError.PATIENT_INFO_REQUIRED);
        }
    }

    private void validateDoctorInfo(CreateUserRequest request) {
        if (!StringUtils.hasText(request.department()) ||
                !StringUtils.hasText(request.specialization())) {
            throw new AppException(ValidationError.DOCTOR_INFO_REQUIRED);
        }
    }
}
