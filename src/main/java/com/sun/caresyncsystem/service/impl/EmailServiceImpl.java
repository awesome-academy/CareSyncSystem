package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.configuration.SecurityProperties;
import com.sun.caresyncsystem.service.EmailService;
import com.sun.caresyncsystem.utils.MessageUtil;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MessageUtil messageUtil;
    private final SecurityProperties securityProperties;

    @Async
    @Override
    public void sendActivationEmail(String to, String name, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(messageUtil.getMessage("mail.activation.subject"));
        message.setText(messageUtil.getMessage("mail.activation.content") + activationLink);
        message.setFrom(securityProperties.getMail().getOwner());

        mailSender.send(message);
    }

    @Async
    @Override
    public void sendPendingApprovalEmail(String to, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(messageUtil.getMessage("mail.doctor.pending.subject"));
        message.setText(MessageFormat.format(messageUtil.getMessage("mail.doctor.pending.content"), fullName));
        message.setFrom(securityProperties.getMail().getOwner());

        mailSender.send(message);
    }

    @Async
    @Override
    public void sendRejectDoctorEmail(String to, String fullName, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        String validReason = reason != null ? reason : messageUtil.getMessage("mail.doctor.rejected.content");
        message.setTo(to);
        message.setSubject(messageUtil.getMessage("mail.doctor.rejected.subject"));
        message.setText(MessageFormat.format(
                messageUtil.getMessage("mail.doctor.rejected.content"),
                fullName,
                validReason
        ));
        message.setFrom(securityProperties.getMail().getOwner());

        mailSender.send(message);
    }
}
