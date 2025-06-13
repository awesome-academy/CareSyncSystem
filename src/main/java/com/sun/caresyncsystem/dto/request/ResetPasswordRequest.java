package com.sun.caresyncsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank
        String token,
        @NotBlank
        @Size(min = 8, message = "PASSWORD_INVALID_SIZE")
        String newPassword
) {
}
