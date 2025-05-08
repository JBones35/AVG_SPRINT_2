package kirschner.flaig.mozart.repository;

import java.time.LocalDateTime;

import kirschner.flaig.mozart.entity.DeliveryStatus;
import kirschner.flaig.mozart.entity.Lieferinformationen;

/**
 * Builder-Klasse für die Lieferinformationen-Entität.
 * Ermöglicht eine flexible und lesbare Erstellung von Lieferinformationen-Objekten.
 */
public class LieferinformationenBuilder {

    private LocalDateTime deliveryDate;
    private DeliveryStatus deliveryStatus;

    public static LieferinformationenBuilder getInstance() {
        return new LieferinformationenBuilder();
    }

    /**
     * Setzt das (voraussichtliche oder tatsächliche) Lieferdatum.
     * @param deliveryDate Das Datum und die Uhrzeit der Lieferung.
     * @return Den Builder selbst für Method Chaining.
     */
    public LieferinformationenBuilder withDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    /**
     * Setzt den Lieferstatus.
     * @param deliveryStatus Der aktuelle Status der Lieferung (z.B. Enum-Wert).
     * @return Den Builder selbst für Method Chaining.
     */
    public LieferinformationenBuilder withDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        return this;
    }

    /**
     * Erstellt eine neue Instanz der Klasse Lieferinformationen mit den im Builder gesetzten Werten.
     * Ruft den Konstruktor der Lieferinformationen-Klasse auf.
     * @return Eine neue Lieferinformationen-Instanz.
     */
    public Lieferinformationen build() {
        return new Lieferinformationen(
                this.deliveryDate,
                this.deliveryStatus
        );
    }
}
