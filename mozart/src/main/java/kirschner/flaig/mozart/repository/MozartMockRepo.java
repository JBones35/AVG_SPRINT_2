package kirschner.flaig.mozart.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
// import java.util.Collections; // Import für Collections.unmodifiableMap, falls später benötigt

import kirschner.flaig.mozart.entity.Bestellung;
import kirschner.flaig.mozart.entity.DeliveryStatus;
import kirschner.flaig.mozart.entity.Produktdaten;

/**
 * Stellt öffentlich zugängliche, statische Sammlungen (als Maps implementiert)
 * von {@link Produktdaten} und {@link Bestellung} bereit, die als Mock-Datenquelle dienen.
 * Die Daten werden einmalig beim Laden der Klasse initialisiert.
 * <p>
 * Der Zugriff auf die Daten erfolgt direkt über die statischen Felder:
 * <ul>
 * <li>{@code MozartMockRepo.produktdaten}</li>
 * <li>{@code MozartMockRepo.bestellungen}</li>
 * </ul>
 * Diese Klasse ist als {@code final} deklariert und besitzt einen privaten Konstruktor,
 * um eine Instanziierung zu verhindern.
 */
public final class MozartMockRepo {

    /**
     * Statische Map aller Produktdaten, mit der Produkt-ID als Schlüssel.
     * Die Map wird bei der Initialisierung der Klasse gefüllt.
     * <p>
     * <strong>Hinweis:</strong> Der Inhalt dieser Map ist standardmäßig zur Laufzeit veränderbar.
     * Um dies zu verhindern, könnte die Map nach der Initialisierung mit
     * {@code Collections.unmodifiableMap()} umschlossen werden.
     */
    public static final Map<String, Produktdaten> produktdaten;

    /**
     * Statische Map aller Bestellungen, mit der Bestell-ID als Schlüssel.
     * Die Map wird bei der Initialisierung der Klasse gefüllt.
     * <p>
     * <strong>Hinweis:</strong> Der Inhalt dieser Map ist standardmäßig zur Laufzeit veränderbar.
     */
    public static final Map<String, Bestellung> bestellungen;

    /**
     * Privater Konstruktor, um die Instanziierung dieser Utility-Klasse zu verhindern.
     * Diese Klasse dient ausschließlich als Halter für statische Daten und soll nicht instanziiert werden.
     */
    private MozartMockRepo() {
        // Diese Klasse soll nicht instanziiert werden.
    }

    /**
     * Statischer Initialisierungsblock.
     * Dieser Block wird einmalig ausgeführt, wenn die Java Virtual Machine die Klasse lädt.
     * Er initialisiert die statischen Maps {@link #produktdaten} und {@link #bestellungen} mit Beispieldaten.
     */
    static {
        Map<String, Produktdaten> temporaereProduktdaten = new HashMap<>();
        Map<String, Bestellung> temporaereBestellungen = new HashMap<>();

        // Erstellung und Hinzufügung von Produktdaten
        Produktdaten produkt1 = new ProduktdatenBuilder()
                .withProductId("PROD-MOCK-001")
                .withProductName("Mozart Kugeln Classic (10er)")
                .withCategory("Süßwaren")
                .withPrice(new BigDecimal("5.99"))
                .withStockQuantity(250)
                .build();
        temporaereProduktdaten.put(produkt1.getProduktId(), produkt1);

        Produktdaten produkt2 = new ProduktdatenBuilder()
                .withProductId("PROD-MOCK-002")
                .withProductName("Zauberflöte Partitur")
                .withCategory("Noten")
                .withPrice(new BigDecimal("29.95"))
                .withStockQuantity(30)
                .build();
        temporaereProduktdaten.put(produkt2.getProduktId(), produkt2);

        Produktdaten produkt3 = new ProduktdatenBuilder()
                .withProductId("PROD-MOCK-003")
                .withProductName("Dirigentenstab Ebenholz")
                .withCategory("Zubehör")
                .withPrice(new BigDecimal("45.00"))
                .withStockQuantity(50)
                .build();
        temporaereProduktdaten.put(produkt3.getProduktId(), produkt3);

        // Datumsdefinitionen für Bestellungen
        LocalDateTime bestelldatum1 = LocalDateTime.of(2025, 4, 15, 10, 30, 0);
        LocalDateTime lieferdatum1 = LocalDateTime.of(2025, 4, 18, 14, 0, 0);
        LocalDateTime bestelldatum2 = LocalDateTime.of(2025, 4, 28, 9, 0, 0);

        // Erstellung und Hinzufügung von Bestellungen
        Bestellung bestellung1 = new BestellungBuilder()
                .withOrderID("ORD-MOCK-2025-001")
                .withCustomerID("CUST-MOCK-SALZBURG")
                .withEmail("info@mozarteum.at")
                .withAddress("Mirabellplatz 1, 5020 Salzburg")
                .withProductID(produkt1.getProduktId())
                .withQuantity(20)
                .withOrderDate(bestelldatum1)
                .withDeliveryStatus(DeliveryStatus.DELIVERED) // Verwendung der englischen Enum-Konstante
                .withDeliveryDate(lieferdatum1)
                .withPaymentMethod("Rechnung")
                .build();
        temporaereBestellungen.put(bestellung1.getBestellId(), bestellung1);

        Bestellung bestellung2 = new BestellungBuilder()
                .withOrderID("ORD-MOCK-2025-002")
                .withCustomerID("CUST-MOCK-WIEN")
                .withEmail("tickets@staatsoper.at")
                .withAddress("Opernring 2, 1010 Wien")
                .withProductID(produkt2.getProduktId())
                .withQuantity(5)
                .withOrderDate(bestelldatum2)
                .withDeliveryStatus(DeliveryStatus.PROCESSING) // Verwendung der englischen Enum-Konstante
                .withPaymentMethod("Kreditkarte")
                .build();
        temporaereBestellungen.put(bestellung2.getBestellId(), bestellung2);

        // Zuweisung der temporären Maps zu den finalen statischen Feldern
        // Optional: Um die Maps unveränderlich zu machen:
        // produktdaten = Collections.unmodifiableMap(temporaereProduktdaten);
        // bestellungen = Collections.unmodifiableMap(temporaereBestellungen);
        produktdaten = temporaereProduktdaten;
        bestellungen = temporaereBestellungen;
    }
}