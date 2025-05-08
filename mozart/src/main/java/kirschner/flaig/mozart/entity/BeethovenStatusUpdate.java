package kirschner.flaig.mozart.entity;

/**
 * Repräsentiert eine Statusaktualisierung für eine Bestellung,
 * typischerweise im Kontext eines Systems oder Moduls namens "Beethoven".
 */
public class BeethovenStatusUpdate {
    /**
     * Die eindeutige Identifikationsnummer der Bestellung.
     */
    private String bestellId;
    /**
     * Der aktuelle Status der Bestellung.
     */
    private String status;

    /**
     * Konstruktor zum Erstellen einer neuen BeethovenStatusUpdate-Instanz.
     *
     * @param bestellId Die Identifikationsnummer der Bestellung.
     * @param status Der neue Status der Bestellung.
     */
    public BeethovenStatusUpdate(String bestellId, String status) {
        this.bestellId = bestellId;
        this.status = status;
    }

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
     * Gibt den Status der Bestellung zurück.
     *
     * @return Der aktuelle Status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setzt den Status der Bestellung.
     *
     * @param status Der neue Status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gibt eine String-Repräsentation des BeethovenStatusUpdate-Objekts zurück.
     *
     * @return Eine Zeichenkette, die das Objekt darstellt.
     */
    @Override
    public String toString() {
        return "BeethovenStatusUpdate{" +
                "bestellId='" + bestellId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}