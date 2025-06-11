package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserProfileResponse;

public interface ProfileService {
    UserProfileResponse getCurrentUserProfile();
    UserProfileResponse updateProfile(UpdateProfileRequest request);
}
