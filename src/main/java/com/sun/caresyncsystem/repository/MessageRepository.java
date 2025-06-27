package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByConversationIdOrderByTimestampAsc(Long conversationId);
}
