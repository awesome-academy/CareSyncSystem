package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.ReviewDoctorRegistrationRequest;
import com.sun.caresyncsystem.dto.request.CreateUserRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    Page<UserResponse> getPendingDoctors(Pageable pageable);
    UserResponse getUserByUserId(Long userId);
    void reviewDoctorRegistration(Long userId, ReviewDoctorRegistrationRequest request);
}
