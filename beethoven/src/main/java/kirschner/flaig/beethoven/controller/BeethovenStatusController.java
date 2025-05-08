package kirschner.flaig.beethoven.controller;

import kirschner.flaig.beethoven.service.BeethovenStatusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller für die Handhabung von Statusanfragen und -aktualisierungen
 * im Kontext des Beethoven-Services. Stellt Endpunkte bereit, um Statusinformationen
 * basierend auf einer ID und einem Statuswert zu senden.
 */
@RestController
@RequestMapping("/api")
public class BeethovenStatusController {

    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(BeethovenStatusController.class);

    /**
     * Service-Klasse, die für die Logik der Statusaktualisierungen zuständig ist.
     */
    private final BeethovenStatusService beethovenStatusService;

    /**
     * Konstruktor für den {@code BeethovenStatusController}.
     *
     * @param beethovenStatusService Der zu injizierende {@link BeethovenStatusService}.
     */
    public BeethovenStatusController(BeethovenStatusService beethovenStatusService) {
        this.beethovenStatusService = beethovenStatusService;
    }

    /**
     * Verarbeitet eine GET-Anfrage, um einen Status für eine bestimmte Bestellabwicklungs-ID zu setzen/senden.
     * Nimmt die ID aus dem Pfad und den Status als Request-Parameter entgegen.
     *
     * @param id Die ID der Bestellabwicklung (aus dem URL-Pfad).
     * @param status Der zu setzende Status (als Request-Parameter, erforderlich).
     * @return Eine {@link ResponseEntity}, die den Erfolg der Operation anzeigt.
     * @throws IllegalArgumentException wenn der Status-Service eine ungültige Eingabe feststellt.
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<String> getStatusForBestellabwicklung(@PathVariable String id,
                                                                @RequestParam(value = "status", required = true) String status) throws IllegalArgumentException {
        LOGGER.info("Start getStatusForBestellabwicklung() mit id: {} und status: {}", id, status);
        // Annahme: Die Methode in BeethovenStatusService heißt/wird umbenannt zu sendeStatusAktualisierung
        this.beethovenStatusService.sendeStatusAktualisierung(id, status);
        LOGGER.info("Ende getStatusForBestellabwicklung() mit id: {} und status: {}", id, status);
        return ResponseEntity.ok("Status update sent for order ID: " + id + " with status: " + status);
    }

    /**
     * Behandelt {@link IllegalArgumentException}, die innerhalb dieses Controllers auftreten.
     * Gibt eine HTTP 400 Bad Request Antwort mit der Fehlermeldung zurück.
     *
     * @param ausnahme Die aufgetretene {@link IllegalArgumentException}.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und der Nachricht der Ausnahme.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> behandleUngueltigesArgumentException(IllegalArgumentException ausnahme) {
        LOGGER.error("Fehler aufgetreten: {}", ausnahme.getMessage(), ausnahme);
        return ResponseEntity.badRequest().body(ausnahme.getMessage());
    }
}