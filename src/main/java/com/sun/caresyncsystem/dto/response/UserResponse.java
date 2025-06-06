package com.sun.caresyncsystem.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
    Long id,
    String email,
    String fullName,
    String phone,
    String address,
    LocalDateTime dateOfBirth
) {}
