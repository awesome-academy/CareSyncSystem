package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;

public interface ProfileService {
    UserResponse getCurrentUserProfile(Long userId);
    UserResponse updateProfile(UpdateProfileRequest request, Long userId);
}
