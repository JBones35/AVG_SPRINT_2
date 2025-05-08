package kirschner.flaig.beethoven.repository;

import java.time.LocalDateTime;

import kirschner.flaig.beethoven.entity.Bestellabwicklung;
import kirschner.flaig.beethoven.entity.OrderStatus; // Annahme: Enum existiert

/**
 * Ein Erbauer (Builder) zur schrittweisen Erstellung von {@link Bestellabwicklung}-Objekten.
 * Verwendet das Erbauer-Muster, um die Objekterstellung zu vereinfachen und
 * die Lesbarkeit zu verbessern. Instanzen werden über die statische
 * Fabrikmethode {@link #erhalteInstanz()} bezogen.
 */
public class BestellabwicklungBuilder {

    /**
     * Die Bestell-ID für das zu erstellende {@link Bestellabwicklung}-Objekt.
     */
    private String bestellId;
    /**
     * Die Kunden-ID für das zu erstellende {@link Bestellabwicklung}-Objekt.
     */
    private String kundenId;
    /**
     * Die Produkt-ID für das zu erstellende {@link Bestellabwicklung}-Objekt.
     */
    private String produktId;
    /**
     * Der Bestellstatus für das zu erstellende {@link Bestellabwicklung}-Objekt.
     */
    private OrderStatus bestellStatus; // Annahme: OrderStatus ist der Name des Enums
    /**
     * Das Versanddatum für das zu erstellende {@link Bestellabwicklung}-Objekt.
     */
    private LocalDateTime versanddatum;

    /**
     * Privater Konstruktor, um die direkte Instanziierung zu verhindern.
     * Verwenden Sie {@link #erhalteInstanz()}.
     */
    private BestellabwicklungBuilder() {
    }

    /**
     * Gibt eine neue Instanz des Erbauers zurück.
     * Dies ist die Fabrikmethode für den {@code BestellabwicklungBuilder}.
     *
     * @return eine neue {@code BestellabwicklungBuilder}-Instanz.
     */
    public static BestellabwicklungBuilder erhalteInstanz() {
        return new BestellabwicklungBuilder();
    }

    /**
     * Setzt die Bestell-ID für das zu erstellende Objekt.
     *
     * @param bestellId die eindeutige ID der Bestellung.
     * @return diese {@code BestellabwicklungBuilder}-Instanz für Method Chaining (Verkettung von Aufrufen).
     */
    public BestellabwicklungBuilder mitBestellId(String bestellId) {
        this.bestellId = bestellId;
        return this;
    }

    /**
     * Setzt die Kunden-ID für das zu erstellende Objekt.
     *
     * @param kundenId die ID des Kunden.
     * @return diese {@code BestellabwicklungBuilder}-Instanz für Method Chaining.
     */
    public BestellabwicklungBuilder mitKundenId(String kundenId) {
        this.kundenId = kundenId;
        return this;
    }

    /**
     * Setzt die Produkt-ID für das zu erstellende Objekt.
     *
     * @param produktId die ID des bestellten Produkts.
     * @return diese {@code BestellabwicklungBuilder}-Instanz für Method Chaining.
     */
    public BestellabwicklungBuilder mitProduktId(String produktId) {
        this.produktId = produktId;
        return this;
    }

    /**
     * Setzt den Bestellstatus für das zu erstellende Objekt.
     *
     * @param bestellStatus der Status der Bestellung (siehe {@link OrderStatus}).
     * @return diese {@code BestellabwicklungBuilder}-Instanz für Method Chaining.
     */
    public BestellabwicklungBuilder mitBestellStatus(OrderStatus bestellStatus) {
        this.bestellStatus = bestellStatus;
        return this;
    }

    /**
     * Setzt das Versanddatum für das zu erstellende Objekt.
     *
     * @param versanddatum das Datum und die Uhrzeit des Versands.
     * @return diese {@code BestellabwicklungBuilder}-Instanz für Method Chaining.
     */
    public BestellabwicklungBuilder mitVersanddatum(LocalDateTime versanddatum) {
        this.versanddatum = versanddatum;
        return this;
    }

    /**
     * Erstellt und gibt eine neue Instanz von {@link Bestellabwicklung} zurück,
     * basierend auf den zuvor im Erbauer gesetzten Werten.
     * Diese Methode verwendet die öffentlichen Set-Methoden der {@code Bestellabwicklung}-Klasse,
     * welche bereits an die deutschen Bezeichner angepasst wurden (z.B. {@code setBestellId}).
     *
     * @return eine neue, konfigurierte {@code Bestellabwicklung}-Instanz.
     */
    public Bestellabwicklung erstellen() {
        Bestellabwicklung abwicklung = new Bestellabwicklung();
        abwicklung.setBestellId(this.bestellId);
        abwicklung.setKundenId(this.kundenId);
        abwicklung.setProduktId(this.produktId);
        abwicklung.setBestellStatus(this.bestellStatus);
        abwicklung.setVersanddatum(this.versanddatum);
        return abwicklung;
    }
}