package com.appdashboard.features.Contact;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ContactService {

    private final MailSender mailSender;
    private final String destinationEmail;

    public ContactService(MailSender mailSender, @Value("${custom.contact-email}") String destinationEmail) {
        this.mailSender = mailSender;
        this.destinationEmail = destinationEmail;
    }

    public void sendContactEmail(ContactRequestDTO request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinationEmail);
        message.setSubject("NOUVEAU CONTACT MAJOR BASELINE : " + request.subject());
        message.setText("Nouveau message envoyé depuis Major Baseline.\n\n" +
                "Email de contact : " + request.email() + "\n\n" +
                "Sujet : " + request.subject() + "\n\n" +
                "Message :\n" + request.body());

        mailSender.send(message);
    }
}
