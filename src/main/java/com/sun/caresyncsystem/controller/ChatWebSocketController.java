package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.configuration.CustomJwtDecoder;
import com.sun.caresyncsystem.dto.request.MessageRequest;
import com.sun.caresyncsystem.dto.response.MessageResponse;
import com.sun.caresyncsystem.service.ChatService;
import com.sun.caresyncsystem.utils.api.UserApiPaths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomJwtDecoder customJwtDecoder;

    @MessageMapping(UserApiPaths.Chat.SEND)
    public void handleMessage(SimpMessageHeaderAccessor accessor, @Payload MessageRequest request) {
        MessageResponse response = chatService.saveMessage(request);
        messagingTemplate.convertAndSend(UserApiPaths.Chat.WEBSOCKET_BROKER + response.conversationId(), response);
    }
}
