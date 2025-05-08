package kirschner.flaig.beethoven.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kirschner.flaig.beethoven.entity.Bestellabwicklung;
import kirschner.flaig.beethoven.entity.OrderStatus;
import kirschner.flaig.beethoven.entity.Produktverwaltung;
import kirschner.flaig.beethoven.grpc.BestellungRequest;
import kirschner.flaig.beethoven.grpc.BestellungResponse;
import kirschner.flaig.beethoven.grpc.ErpOrderServiceGrpc;
import kirschner.flaig.beethoven.repository.BeethovenMockRepo;
import kirschner.flaig.beethoven.repository.BestellabwicklungBuilder;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * Implementiert den gRPC-Service {@link ErpOrderServiceGrpc.ErpOrderServiceImplBase}
 * zur Verarbeitung von Bestellungen im ERP-System (Beethoven).
 * Diese Klasse handhabt eingehende Bestellwünsche, prüft Produktverfügbarkeiten,
 * aktualisiert Lagerbestände und legt Bestellabwicklungsdatensätze an.
 */
@GrpcService
public class ErpOrderService extends ErpOrderServiceGrpc.ErpOrderServiceImplBase {
    /**
     * Logger für diese Klasse.
     */
    private static final Logger LOGGER = LogManager.getLogger(ErpOrderService.class);

    /**
     * Konstante für die voraussichtliche Lieferzeit in Tagen, die zu Bestellungen hinzugefügt wird.
     */
    private static final int VORRAUSICHTLICHE_LIEFERZEIT_IN_TAGEN = 3;

    /**
     * Standardkonstruktor für den {@code ErpOrderService}.
     */
    public ErpOrderService() {
        super();
    }

    /**
     * Verarbeitet eine eingehende Bestellanfrage vom gRPC-Client.
     * Validiert das angefragte Produkt, aktualisiert dessen Lagerbestand,
     * erstellt eine neue Bestellabwicklung und sendet eine Antwort an den Client.
     *
     * @param request Die {@link BestellungRequest} vom Client.
     * @param responseObserver Der {@link StreamObserver} für das Senden der {@link BestellungResponse}.
     */
    @Override
    public void bestellungOrder(BestellungRequest request, StreamObserver<BestellungResponse> responseObserver) {
        LOGGER.info("ErpOrderService: bestellungOrder aufgerufen für Produkt-ID {}...", request.getProductId());
        Produktverwaltung produkt = BeethovenMockRepo.produktverwaltung.get(request.getProductId());

        if (!verarbeiteProdukt(produkt, request, responseObserver)) {
            // Die Methode verarbeiteProdukt hat bereits responseObserver.onError aufgerufen.
            return;
        }

        LocalDateTime versanddatum = LocalDateTime.now().plusDays(VORRAUSICHTLICHE_LIEFERZEIT_IN_TAGEN);
        // Annahme: OrderStatus.PROCESSED ist ein gültiger Enum-Wert
        OrderStatus bestellStatus = OrderStatus.PROCESSED;
        UUID bestellId = UUID.randomUUID();

        speichereBestellabwicklungInRepo(bestellId, request, versanddatum, bestellStatus);

        BestellungResponse antwort = BestellungResponse.newBuilder()
                .setOrderId(bestellId.toString())
                .setDeliveryDate(versanddatum.toString())
                .setDeliveryStatus("Processing") // Status-String für die gRPC-Antwort
                .build();

        responseObserver.onNext(antwort);
        responseObserver.onCompleted();
        LOGGER.info("ErpOrderService: Bestellung für Produkt-ID {} erfolgreich bearbeitet. Bestellabwicklungs-ID: {}", request.getProductId(), bestellId);
    }

    /**
     * Verarbeitet das Produkt im Rahmen einer Bestellanfrage. Prüft die Verfügbarkeit
     * und aktualisiert den Lagerbestand. Sendet im Fehlerfall eine entsprechende gRPC-Fehlerantwort.
     *
     * @param produkt Das {@link Produktverwaltung}-Objekt.
     * @param request Die {@link BestellungRequest} vom Client.
     * @param responseObserver Der {@link StreamObserver} für das Senden von gRPC-Fehlerantworten.
     * @return {@code true}, wenn das Produkt erfolgreich verarbeitet wurde, sonst {@code false}.
     */
    private boolean verarbeiteProdukt(Produktverwaltung produkt, BestellungRequest request, StreamObserver<BestellungResponse> responseObserver) {
        if (produkt == null) {
            LOGGER.error("ErpOrderService: Produkt mit ID {} nicht gefunden.", request.getProductId());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Produkt mit ID " + request.getProductId() + " nicht gefunden.")
                    .asRuntimeException());
            return false;
        }

        int neuerLagerbestand = produkt.getLagerbestand() - request.getQuantity();
        if (neuerLagerbestand < 0) {
            LOGGER.error("ErpOrderService: Nicht genügend Lagerbestand für Produkt {}. Benötigt: {}, Verfügbar: {}.",
                    request.getProductId(), request.getQuantity(), produkt.getLagerbestand());
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Nicht genügend Lagerbestand für Produkt " + request.getProductId() +
                            ". Benötigt: " + request.getQuantity() + ", Verfügbar: " + produkt.getLagerbestand())
                    .asRuntimeException());
            return false;
        }
        produkt.setLagerbestand(neuerLagerbestand);
        LOGGER.info("ErpOrderService: Lagerbestand für Produkt {} aktualisiert. Neuer Lagerbestand: {}.", request.getProductId(), neuerLagerbestand);
        return true;
    }

    /**
     * Erstellt eine neue {@link Bestellabwicklung} und speichert sie im Mock-Repository.
     *
     * @param bestellId Die zu verwendende UUID für die Bestellabwicklung.
     * @param request Die ursprüngliche {@link BestellungRequest}.
     * @param versanddatum Das berechnete Versanddatum.
     * @param bestellStatus Der initiale {@link OrderStatus} der Bestellung.
     */
    private void speichereBestellabwicklungInRepo(UUID bestellId, BestellungRequest request, LocalDateTime versanddatum, OrderStatus bestellStatus) {
        Bestellabwicklung bestellabwicklung = BestellabwicklungBuilder.erhalteInstanz()
                .mitBestellId(bestellId.toString())
                .mitKundenId(request.getCustomerId())
                .mitProduktId(request.getProductId())
                .mitBestellStatus(bestellStatus)
                .mitVersanddatum(versanddatum)
                .erstellen();
        BeethovenMockRepo.bestellabwicklung.put(bestellId.toString(), bestellabwicklung);
        LOGGER.info("ErpOrderService: Bestellabwicklung mit ID {} im Repository gespeichert.", bestellId);
    }
}