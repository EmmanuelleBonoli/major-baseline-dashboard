package com.appdashboard.features.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequestDTO(
                @NotBlank(message = "L'adresse email est obligatoire") @Email(message = "Le format de l'adresse email est invalide") String email,

                @NotBlank(message = "L'intitulé est obligatoire") String subject,

                @NotBlank(message = "Le corps du message est obligatoire") String body) {
}
