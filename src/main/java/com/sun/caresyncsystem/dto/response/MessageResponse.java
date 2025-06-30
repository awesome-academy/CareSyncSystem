package com.sun.caresyncsystem.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageResponse(
        Long conversationId,
        Long senderId,
        String senderRole,
        String content,
        LocalDateTime timestamp
) {
}
