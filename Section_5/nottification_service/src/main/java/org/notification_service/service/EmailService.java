package org.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.notification_service.entity.NotificationLog;
import org.notification_service.entity.NotificationStatus;
import org.notification_service.repository.NotificationLogRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final NotificationLogRepository notificationLogRepository;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            saveLog(to, subject, text, NotificationStatus.SENT);
        } catch (Exception ex) {
            saveLog(to, subject, text, NotificationStatus.FAILED);
            throw ex;
        }
    }

    public void sendUserCreationEmail(String email) {
        sendEmail(
                email,
                "Аккаунт создан",
                "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."
        );
    }

    public void sendUserDeletionEmail(String email) {
        sendEmail(
                email,
                "Аккаунт удалён",
                "Здравствуйте! Ваш аккаунт был удалён."
        );
    }

    private void saveLog(String to, String subject, String text, NotificationStatus status) {
        notificationLogRepository.save(
                NotificationLog.builder()
                        .recipientEmail(to)
                        .subject(subject)
                        .messageBody(text)
                        .status(status)
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}