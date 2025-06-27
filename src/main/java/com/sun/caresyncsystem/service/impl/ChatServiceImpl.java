package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.dto.request.MessageRequest;
import com.sun.caresyncsystem.dto.response.MessageResponse;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.mapper.ToDtoMappers;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.model.entity.Conversation;
import com.sun.caresyncsystem.model.entity.Message;
import com.sun.caresyncsystem.model.enums.UserRole;
import com.sun.caresyncsystem.repository.ConversationRepository;
import com.sun.caresyncsystem.repository.DoctorRepository;
import com.sun.caresyncsystem.repository.MessageRepository;
import com.sun.caresyncsystem.repository.UserRepository;
import com.sun.caresyncsystem.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public Long getConversationId(Long senderId, Long receiverId) {
        validateUsersExist(senderId, receiverId);

        boolean isSenderDoctor = doctorRepository.existsByUserId(senderId);
        boolean isReceiverDoctor = doctorRepository.existsByUserId(receiverId);

        Long doctorId, patientId;

        if (isSenderDoctor ^ isReceiverDoctor) {
            doctorId = isSenderDoctor ? senderId : receiverId;
            patientId = isSenderDoctor ? receiverId : senderId;
        } else {
            throw new AppException(ErrorCode.INVALID_ROLE_FOR_CONVERSATION);
        }

        return getOrCreateConversation(doctorId, patientId).getId();
    }

    @Override
    public MessageResponse saveMessage(MessageRequest messageRequest) {
        Long receiverId = messageRequest.receiverId();
        Long senderId = messageRequest.senderId();

        validateUsersExist(senderId, receiverId);

        String senderRoleStr = userRepository.findRoleById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        UserRole senderRole = UserRole.valueOf(senderRoleStr.toUpperCase());
        boolean isDoctorSender = senderRole == UserRole.DOCTOR;

        Long doctorId = isDoctorSender ? senderId : receiverId;
        Long patientId = isDoctorSender ? receiverId : senderId;

        Conversation conversation = getOrCreateConversation(doctorId, patientId);

        Message message = Message.builder()
                .conversation(conversation)
                .senderId(senderId)
                .content(messageRequest.content())
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        return ToDtoMappers.toMessageResponse(message, senderRole.name());
    }

    @Override
    public List<MessageResponse> getMessageHistory(Long conversationId, Long senderId, Long receiverId) {
        validateUsersExist(senderId, receiverId);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        Long doctorId = conversation.getDoctorId();
        Long patientId = conversation.getPatientId();

        if (!senderId.equals(doctorId) && !senderId.equals(patientId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<Message> messages = messageRepository.findAllByConversationIdOrderByTimestampAsc(conversationId);

        return messages.stream()
                .map(msg -> {
                    String role = msg.getSenderId().equals(doctorId) ? UserRole.DOCTOR.name() : UserRole.PATIENT.name();
                    return ToDtoMappers.toMessageResponse(msg, role);
                })
                .toList();
    }

    private void validateUsersExist(Long... userIds) {
        Stream.of(userIds).forEach(id -> {
            if (!userRepository.existsById(id)) {
                throw new AppException(ErrorCode.USER_NOT_EXIST);
            }
        });
    }

    private Conversation getOrCreateConversation(Long doctorId, Long patientId) {
        return conversationRepository.findByDoctorIdAndPatientId(doctorId, patientId)
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder()
                                .doctorId(doctorId)
                                .patientId(patientId)
                                .build()
                ));
    }
}
