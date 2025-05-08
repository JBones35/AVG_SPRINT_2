package kirschner.flaig.beethoven.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import kirschner.flaig.beethoven.entity.Bestellabwicklung;
import kirschner.flaig.beethoven.entity.OrderStatus;
import kirschner.flaig.beethoven.entity.Produktverwaltung;


/**
 * Stellt öffentlich zugängliche, statische Sammlungen (als Maps implementiert)
 * von {@link Produktverwaltung} und {@link Bestellabwicklung} bereit, die als Mock-Datenquelle dienen.
 * Die Daten werden einmalig beim Laden der Klasse initialisiert.
 * <p>
 * Der Zugriff auf die Daten erfolgt direkt über die statischen Felder:
 * <ul>
 * <li>{@code BeethovenMockRepo.produktverwaltung}</li>
 * <li>{@code BeethovenMockRepo.bestellabwicklung}</li>
 * </ul>
 * Diese Klasse ist als {@code final} deklariert und besitzt einen privaten Konstruktor,
 * um eine Instanziierung zu verhindern.
 */
public final class BeethovenMockRepo {

    /**
     * Statische Map aller Produktverwaltungsdaten, mit der Produkt-ID als Schlüssel.
     * Die Map wird bei der Initialisierung der Klasse gefüllt.
     * <p>
     * <strong>Hinweis:</strong> Der Inhalt dieser Map ist standardmäßig zur Laufzeit veränderbar.
     * Um dies zu verhindern, könnte die Map nach der Initialisierung mit
     * {@code Collections.unmodifiableMap()} umschlossen werden.
     */
    public static final Map<String, Produktverwaltung> produktverwaltung;

    /**
     * Statische Map aller Bestellabwicklungen, mit der Bestell-ID als Schlüssel.
     * Die Map wird bei der Initialisierung der Klasse gefüllt.
     * <p>
     * <strong>Hinweis:</strong> Der Inhalt dieser Map ist standardmäßig zur Laufzeit veränderbar.
     */
    public static final Map<String, Bestellabwicklung> bestellabwicklung;

    /**
     * Privater Konstruktor, um die Instanziierung dieser Utility-Klasse zu verhindern.
     * Diese Klasse dient ausschließlich als Halter für statische Daten und soll nicht instanziiert werden.
     */
    private BeethovenMockRepo() {
        // Diese Klasse soll nicht instanziiert werden.
    }

    /**
     * Statischer Initialisierungsblock.
     * Dieser Block wird einmalig ausgeführt, wenn die Java Virtual Machine die Klasse lädt.
     * Er initialisiert die statischen Maps {@link #produktverwaltung} und {@link #bestellabwicklung} mit Beispieldaten.
     */
    static {
        Map<String, Produktverwaltung> temporaereProduktverwaltung = new HashMap<>();
        Map<String, Bestellabwicklung> temporaereBestellabwicklung = new HashMap<>();

        Produktverwaltung produktVerwaltung1 = ProduktverwaltungBuilder.erhalteInstanz()
                .mitProduktId("PROD-MOCK-001")
                .mitProduktName("Mozart Kugeln Classic (10er)")
                .mitLieferant("Breitkopf & Härtel")
                .mitEinkaufspreis(new BigDecimal("3.50"))
                .mitVerkaufspreis(new BigDecimal("5.99"))
                .mitLagerbestand(250)
                .erstellen();
        temporaereProduktverwaltung.put(produktVerwaltung1.getProduktId(), produktVerwaltung1);

        Produktverwaltung produktVerwaltung2 = ProduktverwaltungBuilder.erhalteInstanz()
                .mitProduktId("PROD-MOCK-002")
                .mitProduktName("Zauberflöte Partitur")
                .mitLieferant("G. Henle Verlag")
                .mitEinkaufspreis(new BigDecimal("12.00"))
                .mitVerkaufspreis(new BigDecimal("29.95"))
                .mitLagerbestand(30)
                .erstellen();
        temporaereProduktverwaltung.put(produktVerwaltung2.getProduktId(), produktVerwaltung2);

        Produktverwaltung produktVerwaltung3 = ProduktverwaltungBuilder.erhalteInstanz()
                .mitProduktId("PROD-MOCK-003")
                .mitProduktName("Dirigentenstab Ebenholz")
                .mitLieferant("Kunstgießerei Bonn")
                .mitEinkaufspreis(new BigDecimal("25.00"))
                .mitVerkaufspreis(new BigDecimal("45.00"))
                .mitLagerbestand(50)
                .erstellen();
        temporaereProduktverwaltung.put(produktVerwaltung3.getProduktId(), produktVerwaltung3);

        Bestellabwicklung bestellabwicklung1 = BestellabwicklungBuilder.erhalteInstanz()
                .mitBestellId("ORD-MOCK-2025-001")
                .mitKundenId("CUST-MOCK-SALZBURG")
                .mitProduktId(produktVerwaltung1.getProduktId())
                .mitBestellStatus(OrderStatus.SHIPPED)
                .mitVersanddatum(LocalDateTime.of(2025, 4, 28, 16, 0, 0))
                .erstellen();
        temporaereBestellabwicklung.put(bestellabwicklung1.getBestellId(), bestellabwicklung1);

        Bestellabwicklung bestellabwicklung2 = BestellabwicklungBuilder.erhalteInstanz()
                .mitBestellId("ORD-MOCK-2025-002")
                .mitKundenId("CUST-MOCK-WIEN")
                .mitProduktId(produktVerwaltung3.getProduktId())
                .mitBestellStatus(OrderStatus.PROCESSED)
                .erstellen();
        temporaereBestellabwicklung.put(bestellabwicklung2.getBestellId(), bestellabwicklung2);
        produktverwaltung = temporaereProduktverwaltung;
        bestellabwicklung = temporaereBestellabwicklung;
    }
}