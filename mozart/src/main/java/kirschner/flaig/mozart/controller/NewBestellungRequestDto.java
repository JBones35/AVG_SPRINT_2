package kirschner.flaig.mozart.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) für den Empfang neuer Bestellanfragen
 * über die REST-API im E-Commerce-Dienst.
 */
public record NewBestellungRequestDto(
        /**
         * Die eindeutige Identifikationsnummer des Kunden.
         * Darf nicht leer sein.
         */
        @NotEmpty(message = "Customer ID cannot be empty")
        String kundenId,

        /**
         * Die E-Mail-Adresse des Kunden.
         * Muss ein gültiges E-Mail-Format haben und darf nicht leer sein.
         */
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email,

        /**
         * Die Lieferadresse für die Bestellung.
         * Darf nicht leer sein.
         */
        @NotEmpty(message = "Address cannot be empty")
        String adresse,

        /**
         * Die eindeutige Identifikationsnummer des bestellten Produkts.
         * Darf nicht leer sein.
         */
        @NotEmpty(message = "Product ID cannot be empty")
        String produktId,

        /**
         * Die bestellte Menge des Produkts.
         * Darf nicht null sein und muss mindestens 1 betragen.
         */
        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer menge,

        /**
         * Die gewählte Zahlungsmethode für die Bestellung.
         * Darf nicht leer sein.
         */
        @NotEmpty(message = "Payment method cannot be empty")
        String zahlungsmethode
) {
}