package kirschner.flaig.mozart.entity;

import java.time.LocalDateTime;

/**
 * Enthält Informationen zu einer Lieferung, insbesondere das Lieferdatum und den Lieferstatus.
 * Diese Klasse dient dazu, Lieferdetails gebündelt darzustellen und zu übertragen.
 */
public class Lieferinformationen {

    /**
     * Das Datum und die Uhrzeit der Lieferung.
     */
    private LocalDateTime lieferdatum;

    /**
     * Der aktuelle Status der Lieferung (z.B. versendet, zugestellt).
     * Verwendet das Enum {@link DeliveryStatus}.
     */
    private DeliveryStatus lieferstatus;

    /**
     * Konstruktor zur Erstellung einer neuen Instanz von {@code Lieferinformationen}.
     *
     * @param lieferdatum Das Datum und die Uhrzeit der Lieferung.
     * @param lieferstatus Der Status der Lieferung.
     */
    public Lieferinformationen(LocalDateTime lieferdatum, DeliveryStatus lieferstatus) {
        this.lieferdatum = lieferdatum;
        this.lieferstatus = lieferstatus;
    }

    /**
     * Gibt das Lieferdatum und die Lieferzeit zurück.
     *
     * @return Das {@link LocalDateTime} der Lieferung.
     */
    public LocalDateTime getLieferdatum() {
        return lieferdatum;
    }

    /**
     * Setzt das Lieferdatum und die Lieferzeit.
     *
     * @param lieferdatum Das neue {@link LocalDateTime} der Lieferung.
     */
    public void setLieferdatum(LocalDateTime lieferdatum) {
        this.lieferdatum = lieferdatum;
    }

    /**
     * Gibt den aktuellen Lieferstatus zurück.
     *
     * @return Der {@link DeliveryStatus} der Lieferung.
     */
    public DeliveryStatus getLieferstatus() {
        return lieferstatus;
    }

    /**
     * Setzt den aktuellen Lieferstatus.
     *
     * @param lieferstatus Der neue {@link DeliveryStatus} der Lieferung.
     */
    public void setLieferstatus(DeliveryStatus lieferstatus) {
        this.lieferstatus = lieferstatus;
    }

    /**
     * Gibt eine String-Repräsentation des {@code Lieferinformationen}-Objekts zurück.
     * Dies ist nützlich für Debugging- und Logging-Zwecke.
     *
     * @return Eine Zeichenkette, die das Objekt mit seinem Lieferdatum und Lieferstatus darstellt.
     */
    @Override
    public String toString() {
        return "Lieferinformationen{" +
                "lieferdatum=" + lieferdatum +
                ", lieferstatus=" + lieferstatus +
                '}';
    }
}