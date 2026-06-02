package org.notification_service.controller;

import lombok.RequiredArgsConstructor;
import org.notification_service.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/send-creation-email")
    public ResponseEntity<String> sendCreationEmail(@RequestParam String email) {
        emailService.sendUserCreationEmail(email);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/send-deletion-email")
    public ResponseEntity<String> sendDeletionEmail(@RequestParam String email) {
        emailService.sendUserDeletionEmail(email);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/send-custom-email")
    public ResponseEntity<String> sendCustomEmail(
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String text) {
        emailService.sendEmail(email, subject, text);
        return ResponseEntity.ok("Email sent successfully");
    }
}
