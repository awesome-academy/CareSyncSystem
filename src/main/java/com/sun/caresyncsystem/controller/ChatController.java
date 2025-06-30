package com.sun.caresyncsystem.controller;

import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.dto.response.MessageResponse;
import com.sun.caresyncsystem.service.ChatService;
import com.sun.caresyncsystem.utils.JwtUtil;
import com.sun.caresyncsystem.utils.api.UserApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(UserApiPaths.Chat.BASE)
public class ChatController {

    private final ChatService chatService;

    @GetMapping(UserApiPaths.Chat.CONVERSATION_ID)
    public ResponseEntity<BaseApiResponse<Long>> getOrCreateConversation(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long receiverId
    ) {
        Long senderId = JwtUtil.extractUserIdFromJwt(jwt);
        Long conversationId = chatService.getConversationId(senderId, receiverId);

        return ResponseEntity.ok(new BaseApiResponse<>(
                HttpStatus.OK.value(),
                conversationId
        ));
    }

    @GetMapping(UserApiPaths.Chat.HISTORY)
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getChatHistory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long receiverId,
            @RequestParam Long conversationId) {
        Long senderId = JwtUtil.extractUserIdFromJwt(jwt);

        return ResponseEntity.ok(chatService.getMessageHistory(conversationId, senderId, receiverId));
    }
}
