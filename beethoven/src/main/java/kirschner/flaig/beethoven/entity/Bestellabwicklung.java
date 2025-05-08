package kirschner.flaig.beethoven.entity;

import java.time.LocalDateTime;

// Annahme: Das Enum OrderStatus existiert in diesem Paket oder ist importiert.
// import kirschner.flaig.beethoven.entity.OrderStatus;

/**
 * Repräsentiert den Prozess der Abwicklung einer Bestellung im Beethoven-System.
 * Enthält Informationen wie Bestell-, Kunden- und Produktidentifikatoren,
 * den aktuellen Bestellstatus und das Versanddatum.
 */
public class Bestellabwicklung {

    /**
     * Die eindeutige Identifikationsnummer der Bestellung.
     */
    private String bestellId;

    /**
     * Die eindeutige Identifikationsnummer des Kunden, der die Bestellung aufgegeben hat.
     */
    private String kundenId;

    /**
     * Die eindeutige Identifikationsnummer des bestellten Produkts.
     */
    private String produktId;

    /**
     * Der aktuelle Status der Bestellung (z.B. IN_BEARBEITUNG, VERSENDET).
     * Verwendet das Enum {@link OrderStatus}.
     */
    private OrderStatus bestellStatus; // Annahme: OrderStatus ist der Name des Enums

    /**
     * Das Datum und die Uhrzeit, zu dem die Bestellung versendet wurde oder werden soll.
     */
    private LocalDateTime versanddatum;

    // Standardkonstruktor wird implizit bereitgestellt, falls kein anderer Konstruktor definiert ist.
    // Falls ein Konstruktor benötigt wird, sollte er hier mit deutschen Parametern hinzugefügt werden.

    /**
     * Gibt die Bestell-ID zurück.
     *
     * @return Die Identifikationsnummer der Bestellung.
     */
    public String getBestellId() {
        return bestellId;
    }

    /**
     * Setzt die Bestell-ID.
     *
     * @param bestellId Die neue Identifikationsnummer der Bestellung.
     */
    public void setBestellId(String bestellId) {
        this.bestellId = bestellId;
    }

    /**
     * Gibt die Kunden-ID zurück.
     *
     * @return Die Identifikationsnummer des Kunden.
     */
    public String getKundenId() {
        return kundenId;
    }

    /**
     * Setzt die Kunden-ID.
     *
     * @param kundenId Die neue Identifikationsnummer des Kunden.
     */
    public void setKundenId(String kundenId) {
        this.kundenId = kundenId;
    }

    /**
     * Gibt die Produkt-ID zurück.
     *
     * @return Die Identifikationsnummer des Produkts.
     */
    public String getProduktId() {
        return produktId;
    }

    /**
     * Setzt die Produkt-ID.
     *
     * @param produktId Die neue Identifikationsnummer des Produkts.
     */
    public void setProduktId(String produktId) {
        this.produktId = produktId;
    }

    /**
     * Gibt den aktuellen Bestellstatus zurück.
     *
     * @return Der {@link OrderStatus} der Bestellung.
     */
    public OrderStatus getBestellStatus() {
        return bestellStatus;
    }

    /**
     * Setzt den aktuellen Bestellstatus.
     * Dies entspricht der Annahme aus dem {@code BeethovenStatusService}.
     *
     * @param bestellStatus Der neue {@link OrderStatus} der Bestellung.
     */
    public void setBestellStatus(OrderStatus bestellStatus) {
        this.bestellStatus = bestellStatus;
    }

    /**
     * Gibt das Versanddatum zurück.
     *
     * @return Das Datum und die Uhrzeit des Versands als {@link LocalDateTime}.
     */
    public LocalDateTime getVersanddatum() {
        return versanddatum;
    }

    /**
     * Setzt das Versanddatum.
     *
     * @param versanddatum Das neue Datum und die Uhrzeit des Versands als {@link LocalDateTime}.
     Löschen Sie das Tracking Ihrer persönlichen Daten, wenn es möglich ist.
     */
    public void setVersanddatum(LocalDateTime versanddatum) {
        this.versanddatum = versanddatum;
    }
}