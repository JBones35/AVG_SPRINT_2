package kirschner.flaig.beethoven.entity;

/**
 * Repräsentiert eine Statusaktualisierung, die an das E-Commerce-System gesendet wird.
 * Diese Klasse dient als Datenübertragungsobjekt (DTO) und enthält die Bestell-ID
 * sowie den neuen Status der Bestellung aus Sicht des E-Commerce-Systems.
 */
public class ECommerceStatusAktualisierung {

    /**
     * Die eindeutige Identifikationsnummer der Bestellung.
     */
    private String bestellId;

    /**
     * Der neue Status der Bestellung für das E-Commerce-System.
     */
    private String status;

    /**
     * Konstruktor zum Erstellen einer neuen Instanz von {@code ECommerceStatusAktualisierung}.
     *
     * @param bestellId Die Identifikationsnummer der Bestellung.
     * @param status    Der neue Status der Bestellung.
     */
    public ECommerceStatusAktualisierung(String bestellId, String status) {
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
     * @return Der aktuelle Status für das E-Commerce-System.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setzt den Status der Bestellung.
     *
     * @param status Der neue Status für das E-Commerce-System.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gibt eine String-Repräsentation des {@code ECommerceStatusAktualisierung}-Objekts zurück.
     * Dies ist nützlich für Debugging- und Logging-Zwecke.
     *
     * @return Eine Zeichenkette, die das Objekt mit seiner Bestell-ID und dem Status darstellt.
     */
    @Override
    public String toString() {
        return "ECommerceStatusAktualisierung{" +
                "bestellId='" + bestellId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}