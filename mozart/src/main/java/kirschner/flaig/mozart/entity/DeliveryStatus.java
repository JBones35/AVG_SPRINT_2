package kirschner.flaig.mozart.entity;

/**
 * Definiert die verschiedenen Zustände, die eine Lieferung während des Versandprozesses annehmen kann.
 * Jeder Status hat einen menschenlesbaren Anzeigenamen.
 */
public enum DeliveryStatus {
    /**
     * Repräsentiert den Status, dass die Sendung versendet wurde.
     * Der Anzeigename ist "Shipped".
     */
    SHIPPED("Shipped"),

    /**
     * Repräsentiert den Status, dass die Sendung erfolgreich an den Empfänger zugestellt wurde.
     * Der Anzeigename ist "Delivered".
     */
    DELIVERED("Delivered"),

    /**
     * Repräsentiert den Status, dass die Bestellung derzeit bearbeitet und für den Versand vorbereitet wird.
     * Der Anzeigename ist "Processing".
     */
    PROCESSING("Processing");

    /**
     * Der menschenlesbare Name des Lieferstatus, wie er angezeigt werden soll.
     */
    private final String displayName;

    /**
     * Konstruktor für den Enum {@code DeliveryStatus}.
     *
     * @param displayName Der menschenlesbare Name, der diesem spezifischen Lieferstatus zugeordnet ist.
     */
    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gibt den menschenlesbaren Namen des Lieferstatus zurück.
     *
     * @return Der Anzeigename des Status (z.B. "Shipped", "Delivered").
     */
    public String getDisplayName() {
        return displayName;
    }
}