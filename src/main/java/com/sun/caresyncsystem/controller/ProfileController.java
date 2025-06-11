package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserProfileResponse;
import com.sun.caresyncsystem.service.ProfileService;
import com.sun.caresyncsystem.utils.api.ProfileApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProfileApiPaths.BASE)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getCurrentUserProfile());
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }
}
