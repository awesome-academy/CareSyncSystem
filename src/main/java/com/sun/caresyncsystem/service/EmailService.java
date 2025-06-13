package com.sun.caresyncsystem.service;

public interface EmailService {
    void sendActivationEmail(String to, String name, String activationLink);
    void sendPendingApprovalEmail(String email, String fullName);
    void sendRejectDoctorEmail(String to, String fullName, String reason);
}
