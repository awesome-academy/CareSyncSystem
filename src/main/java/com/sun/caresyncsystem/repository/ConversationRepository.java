package com.sun.caresyncsystem.repository;

import com.sun.caresyncsystem.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
}
