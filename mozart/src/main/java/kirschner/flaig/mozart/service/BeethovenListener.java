package kirschner.flaig.mozart.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import kirschner.flaig.mozart.config.RabbitMQConfig;
import kirschner.flaig.mozart.entity.BeethovenStatusUpdate;
import kirschner.flaig.mozart.entity.Bestellung;
import kirschner.flaig.mozart.entity.DeliveryStatus; // Enum-Name bleibt DeliveryStatus
import kirschner.flaig.mozart.repository.MozartMockRepo;

/**
 * Ein Service, der auf Nachrichten von einem "Beethoven"-System lauscht,
 * typischerweise um Statusaktualisierungen für Bestellungen zu empfangen und zu verarbeiten.
 * Nutzt RabbitMQ für die Nachrichtenkommunikation.
 */
@Service
public class BeethovenListener {
    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(BeethovenListener.class);

    /**
     * Empfängt Nachrichten von der konfigurierten RabbitMQ-Warteschlange für ERP-System-Updates.
     * Verarbeitet {@link BeethovenStatusUpdate}-Nachrichten, um den Lieferstatus
     * entsprechender Bestellungen im {@link MozartMockRepo} zu aktualisieren.
     *
     * @param statusAktualisierung Das {@link BeethovenStatusUpdate}-Objekt, das aus der Warteschlange empfangen wurde.
     * Enthält die Bestell-ID und den neuen Status.
     */
    @RabbitListener(queues = RabbitMQConfig.ECOMMERCE_STATUS_WARTESCHLANGE_NAME)
    public void receiveMessage(BeethovenStatusUpdate statusAktualisierung) {
        LOGGER.info("BeethovenListener: Nachricht empfangen: {}...", statusAktualisierung.toString());

        if (statusAktualisierung.getBestellId() == null) {
            LOGGER.error("BeethovenListener: Ungültige oder unvollständige Statusaktualisierung empfangen.");
            return;
        }

        Bestellung bestellung = MozartMockRepo.bestellungen.get(statusAktualisierung.getBestellId());

        if (bestellung == null) {
            LOGGER.error("BeethovenListener: Bestellung mit ID {} nicht gefunden.", statusAktualisierung.getBestellId());
            return;
        }

        try {
            DeliveryStatus neuerLieferstatus = DeliveryStatus.valueOf(statusAktualisierung.getStatus().toUpperCase());
            bestellung.setLieferstatus(neuerLieferstatus);
            LOGGER.info("BeethovenListener: Bestellung mit ID {} aktualisiert. Neuer Status: {}...",
                    statusAktualisierung.getBestellId(), statusAktualisierung.getStatus());
        } catch (IllegalArgumentException e) {
            LOGGER.error("BeethovenListener: Ungültiger Statuswert '{}' für Bestellung mit ID {}. Fehler: {}",
                    statusAktualisierung.getStatus(), statusAktualisierung.getBestellId(), e.getMessage());
        }
    }
}