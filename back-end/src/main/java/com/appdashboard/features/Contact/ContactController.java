package com.appdashboard.features.Contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@Valid @RequestBody ContactRequestDTO request) {
        try {
            contactService.sendContactEmail(request);
            return ResponseEntity.ok("Message envoyé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }
}
