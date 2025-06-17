package com.sun.caresyncsystem.service.impl;

import com.sun.caresyncsystem.configuration.SecurityProperties;
import com.sun.caresyncsystem.exception.AppException;
import com.sun.caresyncsystem.exception.ErrorCode;
import com.sun.caresyncsystem.service.EmailService;
import com.sun.caresyncsystem.utils.MessageUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageUtil messageUtil;
    private final SecurityProperties securityProperties;

    @Async
    @Override
    public void sendActivationEmail(String to, String name, String activationLink) {
        String subject = messageUtil.getMessage("mail.activation.subject");
        Map<String, Object> variables = Map.of("name", name, "activationLink", activationLink);
        String content = generateHtmlContent("activation", variables);
        sendHtmlEmail(to, subject, content);
    }

    @Async
    @Override
    public void sendPendingApprovalEmail(String to, String fullName) {
        String subject = messageUtil.getMessage("mail.doctor.pending.subject");
        Map<String, Object> variables = Map.of("fullName", fullName);
        String content = generateHtmlContent("pending-approval", variables);
        sendHtmlEmail(to, subject, content);
    }

    @Async
    @Override
    public void sendRejectDoctorEmail(String to, String fullName, String reason) {
        String subject = messageUtil.getMessage("mail.doctor.rejected.subject");
        String validReason = reason != null ? reason : messageUtil.getMessage("mail.doctor.default.reason");
        Map<String, Object> variables = Map.of("fullName", fullName, "reason", validReason);
        String content = generateHtmlContent("reject-doctor", variables);
        sendHtmlEmail(to, subject, content);
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String fullName, String resetLink) {
        String subject = messageUtil.getMessage("mail.password.reset.subject");

        Map<String, Object> variables = Map.of(
                "fullName", fullName,
                "resetLink", resetLink
        );

        String content = generateHtmlContent("password-reset", variables);
        sendHtmlEmail(to, subject, content);
    }

    @Async
    @Override
    public void sendAccountDeactivatedEmail(String to, String fullName) {
        String subject = messageUtil.getMessage("mail.account.deactivated.subject");
        Map<String, Object> variables = Map.of("fullName", fullName);
        String content = generateHtmlContent("account-deactivated", variables);
        sendHtmlEmail(to, subject, content);
    }

    @Async
    @Override
    public void sendActivationEmailFromAdmin(String to, String fullName) {
        String subject = messageUtil.getMessage("mail.activation.subject");
        Map<String, Object> variables = Map.of("fullName", fullName);
        String content = generateHtmlContent("account-activated-from-admin", variables);
        sendHtmlEmail(to, subject, content);
    }

    private String generateHtmlContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(securityProperties.getMail().getOwner());

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.FAILED_TO_SEND_EMAIL);
        }
    }
}
