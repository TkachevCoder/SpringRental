package com.example.rental.service;

import com.example.rental.model.Rental;
import com.example.rental.model.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }
    public void sendNotification(List<Rental> rentals) {
        for (Rental rental : rentals) {
            User user = rental.getUser();
            if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
                continue;
            }
            try {
                String message = """
                    <h1>Уважаемый %s!</h1>
                    <p>Ваше бронирование #%d подтверждено.</p>
                    """.formatted(user.getUsername(), rental.getId());

                emailService.sendSimpleEmail(user.getEmail(), "Подтверждение бронирования", message);
            } catch (MessagingException e) {
                System.err.println("Ошибка отправки письма: " + e.getMessage());
            }
        }

    }
}
