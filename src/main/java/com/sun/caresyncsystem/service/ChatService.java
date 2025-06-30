package com.sun.caresyncsystem.service;

import com.sun.caresyncsystem.dto.request.MessageRequest;
import com.sun.caresyncsystem.dto.response.MessageResponse;

import java.util.List;

public interface ChatService {
    MessageResponse saveMessage(MessageRequest messageRequest);
    List<MessageResponse> getMessageHistory(Long conversationId, Long senderId, Long receiverId);
    Long getConversationId(Long senderId, Long receiverId);
}
