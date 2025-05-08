package kirschner.flaig.beethoven.service;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kirschner.flaig.beethoven.config.RabbitMQConfig;
import kirschner.flaig.beethoven.entity.Bestellabwicklung;
import kirschner.flaig.beethoven.entity.ECommerceStatusAktualisierung;
import kirschner.flaig.beethoven.entity.OrderStatus;
import kirschner.flaig.beethoven.repository.BeethovenMockRepo;

/**
 * Service-Klasse zur Verarbeitung und Weiterleitung von Statusaktualisierungen
 * für Bestellabwicklungen im Beethoven-System.
 * Validiert Statusänderungen, aktualisiert lokale Repositories und sendet
 * Statusinformationen an das E-Commerce-System via RabbitMQ.
 */
@Service
public class BeethovenStatusService {
    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(BeethovenStatusService.class);

    /**
     * RabbitTemplate für die Kommunikation mit RabbitMQ.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Konstruktor für den {@code BeethovenStatusService}.
     *
     * @param rabbitTemplate Das {@link RabbitTemplate} für die RabbitMQ-Kommunikation.
     */
    @Autowired
    public BeethovenStatusService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Verarbeitet und sendet eine Statusaktualisierung für eine gegebene Bestell-ID.
     * Validiert den Status, aktualisiert die lokale Bestellabwicklung und leitet
     * die Aktualisierung an das E-Commerce-System weiter.
     *
     * @param bestellId Die ID der Bestellung, deren Status aktualisiert werden soll.
     * @param status    Der neue Status als String.
     * @throws IllegalArgumentException Wenn der Status ungültig ist oder die Bestellung nicht gefunden wird.
     */
    public void sendeStatusAktualisierung(String bestellId, String status) throws IllegalArgumentException {
        LOGGER.info("Sende Statusaktualisierung für Bestell-ID: {} mit Status: {}", bestellId, status);
        if (!validiereStatus(status)) {
            LOGGER.error("Ungültiger Status: {}. Statusaktualisierung nicht gesendet.", status);
            throw new IllegalArgumentException("Ungültiger Status: " + status);
        }

        Bestellabwicklung bestellabwicklungAusRepo = BeethovenMockRepo.bestellabwicklung.get(bestellId);
        if (bestellabwicklungAusRepo == null) {
            LOGGER.error("Bestellung mit ID {} nicht gefunden. Statusaktualisierung nicht gesendet.", bestellId);
            throw new IllegalArgumentException("Bestellung mit ID " + bestellId + " nicht gefunden.");
        }

        // Annahme: OrderStatus Enum-Konstanten sind englisch und Bestellabwicklung hat setBestellStatus.
        bestellabwicklungAusRepo.setBestellStatus(OrderStatus.valueOf(status.toUpperCase()));
        sendeStatusAktualisierungAnECommerce(bestellId, status);
        LOGGER.info("Statusaktualisierung für Bestell-ID {} erfolgreich verarbeitet.", bestellId);
    }

    /**
     * Validiert, ob der übergebene Status-String einem gültigen Wert im {@link OrderStatus}-Enum entspricht.
     * Die Validierung ignoriert Groß-/Kleinschreibung.
     *
     * @param status Der zu validierende Status-String.
     * @return {@code true}, wenn der Status gültig ist, andernfalls {@code false}.
     */
    private boolean validiereStatus(String status) {
        LOGGER.info("Validiere Status: {}", status);

        if (status == null) {
            LOGGER.warn("Eingabestatus ist null. Validierung fehlgeschlagen.");
            return false;
        }

        boolean istGueltig = Arrays.stream(OrderStatus.values())
                .anyMatch(os -> os.name().equalsIgnoreCase(status));

        if (istGueltig) {
            LOGGER.info("Status ist gültig: {}", status);
            return true;
        } else {
            LOGGER.error("Ungültiger Status bei Validierung: {}", status);
            return false;
        }
    }

    /**
     * Sendet eine Statusaktualisierung für eine Bestellung an das E-Commerce-System via RabbitMQ.
     * Mappt hierfür den internen ERP-Status auf einen E-Commerce-spezifischen Status.
     *
     * @param bestellId Die ID der Bestellung.
     * @param status    Der interne ERP-Status der Bestellung.
     */
    private void sendeStatusAktualisierungAnECommerce(String bestellId, String status) {
        LOGGER.info("Sende Statusaktualisierung an E-Commerce-System für Bestell-ID: {} mit Status: {}", bestellId, status);
        String statusECommerce = mappeErpStatusZuECommerceStatus(status);
        ECommerceStatusAktualisierung eCommerceAktualisierung = new ECommerceStatusAktualisierung(bestellId, statusECommerce);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ECOMMERCE_EXCHANGE_NAME, RabbitMQConfig.ECOMMERCE_STATUS_ROUTING_KEY, eCommerceAktualisierung);
            LOGGER.info("Statusaktualisierung erfolgreich an E-Commerce-System gesendet für Bestell-ID: {}", bestellId);
        } catch (AmqpException e) {
            LOGGER.error("Fehler beim Senden der Statusaktualisierung an E-Commerce für Bestell-ID {}: {}", bestellId, e.getMessage(), e);
        }
    }

    /**
     * Mappt einen ERP-spezifischen Bestellstatus auf einen für das E-Commerce-System verständlichen Status.
     *
     * @param status Der ERP-Status als String.
     * @return Der gemappte E-Commerce-Status als String.
     * @throws IllegalArgumentException Wenn der ERP-Status unbekannt ist und nicht gemappt werden kann.
     */
    private String mappeErpStatusZuECommerceStatus(String status) {
        LOGGER.info("Mappe ERP-Status '{}' zu E-Commerce-Status.", status);
        switch (status.toUpperCase()) {
            case "SHIPPED" -> {
                return "SHIPPED";
            }
            case "CANCELLED" -> {
                return "DELIVERED";
            }
            case "PROCESSED" -> {
                return "PROCESSING";
            }
            default -> {
                LOGGER.error("Unbekannter ERP-Status '{}' kann nicht auf E-Commerce-Status gemappt werden.", status);
                throw new IllegalArgumentException("Unbekannter Status: " + status);
            }
        }
    }
}