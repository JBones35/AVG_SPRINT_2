package kirschner.flaig.mozart.repository;

import java.time.LocalDateTime;

import kirschner.flaig.mozart.entity.Bestellung;
import kirschner.flaig.mozart.entity.DeliveryStatus;

/**
 * Builder-Klasse für die Bestellung-Entität.
 * Ermöglicht eine flexible und lesbare Erstellung von Bestellung-Objekten.
 */
public class BestellungBuilder {
    private String orderID;
    private String customerID;
    private String email;
    private String address;
    private String productID;
    private int quantity;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime deliveryDate;
    private String paymentMethod;

    public static BestellungBuilder getInstance() {
        return new BestellungBuilder();
    }

    /**
     * Setzt die Bestell-ID.
     * @param orderID Die eindeutige ID der Bestellung.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    /**
     * Setzt die Kunden-ID.
     * @param customerID Die ID des Kunden, der bestellt hat.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withCustomerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    /**
     * Setzt die E-Mail-Adresse des Kunden.
     * @param email Die E-Mail-Adresse.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Setzt die Lieferadresse.
     * @param address Die vollständige Lieferadresse.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Setzt die Produkt-ID.
     * @param productID Die ID des bestellten Produkts.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withProductID(String productID) {
        this.productID = productID;
        return this;
    }

    /**
     * Setzt die Bestellmenge.
     * @param quantity Die Anzahl des bestellten Produkts.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Setzt das Bestelldatum.
     * @param orderDate Das Datum und die Uhrzeit der Bestellung.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    /**
     * Setzt den Lieferstatus.
     * @param deliveryStatus Der aktuelle Status der Lieferung (z.B. Enum-Wert).
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        return this;
    }

    /**
     * Setzt das (voraussichtliche oder tatsächliche) Lieferdatum.
     * @param deliveryDate Das Datum und die Uhrzeit der Lieferung.
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    /**
     * Setzt die Zahlungsmethode.
     * @param paymentMethod Die verwendete Zahlungsmethode (z.B. "Kreditkarte", "PayPal").
     * @return Den Builder selbst für Method Chaining.
     */
    public BestellungBuilder withPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Erstellt eine neue Instanz der Klasse Bestellung mit den im Builder gesetzten Werten.
     * Ruft den Konstruktor der Bestellung-Klasse auf.
     * @return Eine neue Bestellung-Instanz.
     */
    public Bestellung build() {
        return new Bestellung(
                this.orderID,
                this.customerID,
                this.email,
                this.address,
                this.productID,
                this.quantity,
                this.orderDate,
                this.deliveryStatus,
                this.deliveryDate,
                this.paymentMethod
        );
    }
}