package kirschner.flaig.mozart.repository;

import java.math.BigDecimal;

import kirschner.flaig.mozart.entity.Produktdaten;

/**
 * Builder-Klasse für die Produktdaten-Entität.
 * Ermöglicht eine flexible und lesbare Erstellung von Produktdaten-Objekten.
 */
public class ProduktdatenBuilder {
    private String productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private Integer stockQuantity;

    /**
     * Setzt die Produkt-ID.
     * @param productId Die eindeutige ID des Produkts.
     * @return Den Builder selbst für Method Chaining.
     */
    public ProduktdatenBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Setzt den Produktnamen.
     * @param productName Der Name des Produkts.
     * @return Den Builder selbst für Method Chaining.
     */
    public ProduktdatenBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    /**
     * Setzt die Produktkategorie.
     * @param category Die Kategorie, zu der das Produkt gehört.
     * @return Den Builder selbst für Method Chaining.
     */
    public ProduktdatenBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Setzt den Preis des Produkts.
     * @param price Der Preis als BigDecimal.
     * @return Den Builder selbst für Method Chaining.
     */
    public ProduktdatenBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    /**
     * Setzt die Lagermenge.
     * @param stockQuantity Die verfügbare Menge des Produkts auf Lager.
     * @return Den Builder selbst für Method Chaining.
     */
    public ProduktdatenBuilder withStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }

    /**
     * Erstellt eine neue Instanz der Klasse Produktdaten mit den im Builder gesetzten Werten.
     * Ruft den Konstruktor der Produktdaten-Klasse auf.
     * @return Eine neue Produktdaten-Instanz.
     */
    public Produktdaten build() {
        // Hier könnten optional Validierungen hinzugefügt werden,
        // z.B. Prüfung auf Pflichtfelder wie productId oder productName.
        return new Produktdaten(
                this.productId,
                this.productName,
                this.category,
                this.price,
                this.stockQuantity
        );
    }
}