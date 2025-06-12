package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.request.UpdateProfileRequest;
import com.sun.caresyncsystem.dto.response.UserResponse;
import com.sun.caresyncsystem.service.ProfileService;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.api.ProfileApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProfileApiPaths.BASE)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        Long userId = JwtUtil.extractUserIdFromJwt(jwt);
        return ResponseEntity.ok(profileService.getCurrentUserProfile(userId));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody @Valid UpdateProfileRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = JwtUtil.extractUserIdFromJwt(jwt);
        return ResponseEntity.ok(profileService.updateProfile(request, userId));
    }
}
