package kirschner.flaig.mozart.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kirschner.flaig.mozart.entity.Lieferinformationen;
import kirschner.flaig.mozart.service.BestellungService;

/**
 * Controller für die Verarbeitung von Bestellungen.
 * Stellt Endpunkte zum Erstellen neuer Bestellungen und zur Fehlerbehandlung bereit.
 */
@RestController
@RequestMapping("/api/bestellung")
public class BestellungController {

    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(BestellungController.class);

    /**
     * Service-Klasse zur Verarbeitung der Bestelllogik.
     */
    private final BestellungService bestellungService;

    /**
     * Konstruktor für den BestellungController.
     *
     * @param bestellungService Der zu injizierende {@link BestellungService}.
     */
    public BestellungController(BestellungService bestellungService) {
        this.bestellungService = bestellungService;
    }

    /**
     * Verarbeitet eine eingehende Bestellanfrage.
     * Validiert die Anfrage und leitet sie zur Verarbeitung an den {@link BestellungService} weiter.
     *
     * @param bestellAnfrage Das {@link NewBestellungRequestDto} Objekt, das die Bestelldaten enthält.
     * @return Eine {@link ResponseEntity} mit den {@link Lieferinformationen} bei Erfolg oder einem Fehlerstatus.
     * @throws IllegalArgumentException wenn die Eingabedaten ungültig sind.
     */
    @PostMapping
    public ResponseEntity<Lieferinformationen> postBestellung(@Valid @RequestBody NewBestellungRequestDto bestellAnfrage) throws IllegalArgumentException {
        LOGGER.info("BestellungController: starte postBestellung() mit folgendem Objekt: {}...", bestellAnfrage.toString());
        Lieferinformationen antwortDaten = bestellungService.processBestellung(bestellAnfrage);
        LOGGER.info("BestellungController: Bestellung erfolgreich verarbeitet. Antwort: {}...", antwortDaten.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(antwortDaten);
    }

    /**
     * Behandelt {@link IllegalArgumentException}, die im Controller auftreten.
     * Gibt eine HTTP 400 Bad Request Antwort mit der Fehlermeldung zurück.
     *
     * @param ausnahme Die aufgetretene {@link IllegalArgumentException}.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und der Nachricht.
     */
    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ausnahme) {
        LOGGER.error("BestellungController: IllegalArgumentException: {}...", ausnahme.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ausnahme.getMessage());
    }
}