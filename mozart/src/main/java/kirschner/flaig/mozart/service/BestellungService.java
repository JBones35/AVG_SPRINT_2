package kirschner.flaig.mozart.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kirschner.flaig.mozart.config.RabbitMQConfig;
import kirschner.flaig.mozart.controller.NewBestellungRequestDto;
import kirschner.flaig.mozart.entity.Bestellung;
import kirschner.flaig.mozart.entity.BestellungDataSchuhmacher;
import kirschner.flaig.mozart.entity.DeliveryStatus;
import kirschner.flaig.mozart.entity.Lieferinformationen;
import kirschner.flaig.mozart.entity.Produktdaten;
import kirschner.flaig.mozart.grpc.BestellungRequest;
import kirschner.flaig.mozart.grpc.BestellungResponse;
import kirschner.flaig.mozart.grpc.ErpOrderServiceGrpc;
import kirschner.flaig.mozart.repository.BestellungBuilder;
import kirschner.flaig.mozart.repository.LieferinformationenBuilder;
import kirschner.flaig.mozart.repository.MozartMockRepo;
import net.devh.boot.grpc.client.inject.GrpcClient;

/**
 * Service-Klasse für die Verarbeitung von Bestellungen.
 * Diese Klasse ist verantwortlich für die Orchestrierung verschiedener Schritte
 * im Bestellprozess, einschließlich der Kommunikation mit externen Diensten (gRPC, RabbitMQ)
 * und der Aktualisierung von Datenbeständen.
 */
@Service
public class BestellungService {
    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(BestellungService.class);

    /**
     * gRPC-Blocking-Stub für die Kommunikation mit dem Beethoven-ERP-Service.
     */
    @GrpcClient("beethoven-service")
    private ErpOrderServiceGrpc.ErpOrderServiceBlockingStub erpBestellServiceStub;

    /**
     * RabbitTemplate für das Senden von Nachrichten an RabbitMQ.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Konstruktor für den {@code BestellungService}.
     *
     * @param rabbitTemplate Das {@link RabbitTemplate} für die RabbitMQ-Kommunikation.
     */
    @Autowired
    public BestellungService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Verarbeitet eine neue Bestellanfrage.
     * Dieser Prozess beinhaltet die Kommunikation mit einem gRPC-Service, die Aktualisierung
     * des Produktbestands, das Speichern der Bestellung und das Senden einer CRM-Aktualisierung.
     *
     * @param bestellAnfrage Das {@link NewBestellungRequestDto} mit den Daten der neuen Bestellung.
     * @return Die {@link Lieferinformationen} für die verarbeitete Bestellung.
     * @throws IllegalArgumentException Wenn Validierungsfehler auftreten, z.B. nicht genügender Lagerbestand.
     */
    public Lieferinformationen processBestellung(NewBestellungRequestDto bestellAnfrage) throws IllegalArgumentException {
        LOGGER.info("BestellungService: Starte processBestellung() mit folgendem Objekt: {}...", bestellAnfrage.toString());

        BestellungRequest bestellAnfrageGrpc = BestellungRequest.newBuilder()
                .setCustomerId(bestellAnfrage.kundenId())
                .setProductId(bestellAnfrage.produktId())
                .setQuantity(bestellAnfrage.menge())
                .build();

        aendereProduktLagerMenge(bestellAnfrage.produktId(), bestellAnfrage.menge());
        LOGGER.info("BestellungService: Produktbestand aktualisiert für Produkt ID: {}...", bestellAnfrage.produktId());

        BestellungResponse antwortGrpc = erpBestellServiceStub.bestellungOrder(bestellAnfrageGrpc);
        Bestellung bestellung = fuegeBestellungInRepoEin(antwortGrpc, bestellAnfrage);
        sendeCrmAktualisierungsNachricht(bestellung, bestellAnfrage.menge());

        Lieferinformationen lieferinformationen = LieferinformationenBuilder.getInstance()
                .withDeliveryDate(LocalDateTime.parse(antwortGrpc.getDeliveryDate()))
                .withDeliveryStatus(DeliveryStatus.valueOf(antwortGrpc.getDeliveryStatus().toUpperCase()))
                .build();

        LOGGER.info("BestellungService: Bestellung erfolgreich verarbeitet. Lieferinformationen: {}...", lieferinformationen.toString());
        return lieferinformationen;
    }

    /**
     * Ändert die Lagermenge eines Produkts.
     *
     * @param produktId Die ID des Produkts, dessen Lagermenge geändert werden soll.
     * @param anzahl Die Anzahl, um die die Lagermenge reduziert wird.
     * @throws IllegalArgumentException Wenn das Produkt nicht gefunden wird oder der Bestand nicht ausreicht.
     */
    private void aendereProduktLagerMenge(String produktId, int anzahl) throws IllegalArgumentException {
        Produktdaten produktDaten = MozartMockRepo.produktdaten.get(produktId);
        if (produktDaten != null) {
            int neueMenge = produktDaten.getLagermenge() - anzahl;
            if (neueMenge < 0) {
                throw new IllegalArgumentException("Nicht genügend Lagerbestand für Produkt mit ID " + produktId);
            } else {
                produktDaten.setLagermenge(neueMenge);
            }
        } else {
            throw new IllegalArgumentException("Produkt mit ID " + produktId + " nicht gefunden.");
        }
    }

    /**
     * Fügt eine neue Bestellung basierend auf der gRPC-Antwort und der ursprünglichen Anfrage
     * in das Repository ein.
     *
     * @param antwortGrpc Die {@link BestellungResponse} vom gRPC-Service.
     * @param bestellAnfrage Die ursprüngliche {@link NewBestellungRequestDto}.
     * @return Die erstellte und gespeicherte {@link Bestellung}.
     */
    private Bestellung fuegeBestellungInRepoEin(BestellungResponse antwortGrpc, NewBestellungRequestDto bestellAnfrage) {
        LOGGER.info("BestellungService: Erstelle Bestellung mit Order ID: {}...", antwortGrpc.getOrderId());
        Bestellung bestellung = BestellungBuilder.getInstance()
                .withOrderID(antwortGrpc.getOrderId())
                .withCustomerID(bestellAnfrage.kundenId())
                .withEmail(bestellAnfrage.email())
                .withAddress(bestellAnfrage.adresse())
                .withProductID(bestellAnfrage.produktId())
                .withQuantity(bestellAnfrage.menge())
                .withPaymentMethod(bestellAnfrage.zahlungsmethode())
                .withOrderDate(LocalDateTime.now())
                .build();
        MozartMockRepo.bestellungen.put(antwortGrpc.getOrderId(), bestellung);
        LOGGER.info("BestellungService: Bestellung erfolgreich in das Repository eingefügt. Order ID: {}...", antwortGrpc.getOrderId());
        return bestellung;
    }

    /**
     * Sendet eine Aktualisierungsnachricht über die Bestellung an das CRM-System via RabbitMQ.
     *
     * @param bestellung Die {@link Bestellung}, für die eine Aktualisierung gesendet werden soll.
     * @param menge Die bestellte Menge, verwendet zur Berechnung des Gesamtbetrags.
     */
    private void sendeCrmAktualisierungsNachricht(Bestellung bestellung, int menge) {
        try {
            LOGGER.info("BestellungService: Erstelle BestellungDataSchuhmacher für Bestellung mit Order ID: {}...", bestellung.getBestellId());

            BigDecimal gesamtbetrag = berechneGesamtbetrag(bestellung.getProduktId(), menge);
            String anfangsStatus = "Processing";

            BestellungDataSchuhmacher crmAktualisierung = new BestellungDataSchuhmacher(
                    bestellung.getBestellId(),
                    bestellung.getKundenId(),
                    bestellung.getBestelldatum().toLocalDate(),
                    gesamtbetrag,
                    anfangsStatus,
                    bestellung.getEmail(),
                    bestellung.getAdresse()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.CRM_EXCHANGE_NAME, RabbitMQConfig.CRM_ROUTING_KEY , crmAktualisierung);
            LOGGER.info("BestellungService: BestellungDataSchuhmacher erfolgreich erstellt und an die CRM-Queue gesendet. Order ID: {}...", bestellung.getBestellId());
        } catch (AmqpException e) {
            LOGGER.error("Fehler beim Senden der Nachricht an die CRM-Queue: " + e.getMessage(), e);
        }
    }

    /**
     * Berechnet den Gesamtbetrag für eine bestimmte Produktmenge.
     *
     * @param produktId Die ID des Produkts.
     * @param menge Die bestellte Menge.
     * @return Der berechnete Gesamtbetrag als {@link BigDecimal}. Gibt {@link BigDecimal#ZERO} zurück,
     * wenn das Produkt oder sein Preis nicht gefunden werden kann.
     */
    private BigDecimal berechneGesamtbetrag(String produktId, int menge) {
        Produktdaten produktDaten = MozartMockRepo.produktdaten.get(produktId);
        if (produktDaten != null && produktDaten.getPreis() != null) {
            BigDecimal preis = produktDaten.getPreis();
            return preis.multiply(BigDecimal.valueOf(menge));
        } else {
            LOGGER.warn("Produkt mit ID {} hat keinen Preis oder existiert nicht. Gesamtbetrag wird auf 0 gesetzt.", produktId);
            return BigDecimal.ZERO;
        }
    }
}