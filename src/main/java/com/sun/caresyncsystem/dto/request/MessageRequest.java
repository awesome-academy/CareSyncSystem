package com.sun.caresyncsystem.dto.request;

import lombok.Builder;

@Builder
public record MessageRequest(
        Long senderId,
        Long receiverId,
        String content
) {}
