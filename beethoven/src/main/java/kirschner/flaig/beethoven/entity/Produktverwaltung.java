package kirschner.flaig.beethoven.entity;

import java.math.BigDecimal;

/**
 * Repräsentiert Verwaltungsdaten für ein Produkt im Beethoven-System.
 * Diese Klasse enthält Informationen wie Produktidentifikation, Name, Lieferant,
 * Einkaufs- und Verkaufspreise sowie den aktuellen Lagerbestand.
 */
public class Produktverwaltung {

    /**
     * Die eindeutige Identifikationsnummer des Produkts.
     */
    private String produktId;

    /**
     * Der Name des Produkts.
     */
    private String produktName;

    /**
     * Der Lieferant des Produkts.
     */
    private String lieferant;

    /**
     * Der Einkaufspreis des Produkts.
     */
    private BigDecimal einkaufspreis;

    /**
     * Der Verkaufspreis des Produkts.
     */
    private BigDecimal verkaufspreis;

    /**
     * Der aktuelle Lagerbestand des Produkts.
     */
    private int lagerbestand;

    /**
     * Konstruktor zur Erstellung einer neuen Instanz von {@code Produktverwaltung} mit allen Attributen.
     *
     * @param produktId Die eindeutige ID des Produkts.
     * @param produktName Der Name des Produkts.
     * @param lieferant Der Lieferant des Produkts.
     * @param einkaufspreis Der Einkaufspreis des Produkts.
     * @param verkaufspreis Der Verkaufspreis des Produkts.
     * @param lagerbestand Der aktuelle Lagerbestand des Produkts.
     */
    public Produktverwaltung(String produktId, String produktName, String lieferant,
                             BigDecimal einkaufspreis, BigDecimal verkaufspreis, int lagerbestand) {
        this.produktId = produktId;
        this.produktName = produktName;
        this.lieferant = lieferant;
        this.einkaufspreis = einkaufspreis;
        this.verkaufspreis = verkaufspreis;
        this.lagerbestand = lagerbestand;
    }

    /**
     * Gibt die Produkt-ID zurück.
     *
     * @return Die eindeutige Identifikationsnummer des Produkts.
     */
    public String getProduktId() {
        return produktId;
    }

    /**
     * Setzt die Produkt-ID.
     *
     * @param produktId Die neue eindeutige Identifikationsnummer des Produkts.
     */
    public void setProduktId(String produktId) {
        this.produktId = produktId;
    }

    /**
     * Gibt den Produktnamen zurück.
     *
     * @return Der Name des Produkts.
     */
    public String getProduktName() {
        return produktName;
    }

    /**
     * Setzt den Produktnamen.
     *
     * @param produktName Der neue Name des Produkts.
     */
    public void setProduktName(String produktName) {
        this.produktName = produktName;
    }

    /**
     * Gibt den Lieferanten des Produkts zurück.
     *
     * @return Der Name des Lieferanten.
     */
    public String getLieferant() {
        return lieferant;
    }

    /**
     * Setzt den Lieferanten des Produkts.
     *
     * @param lieferant Der neue Name des Lieferanten.
     */
    public void setLieferant(String lieferant) {
        this.lieferant = lieferant;
    }

    /**
     * Gibt den Einkaufspreis des Produkts zurück.
     *
     * @return Der Einkaufspreis als {@link BigDecimal}.
     */
    public BigDecimal getEinkaufspreis() {
        return einkaufspreis;
    }

    /**
     * Setzt den Einkaufspreis des Produkts.
     *
     * @param einkaufspreis Der neue Einkaufspreis als {@link BigDecimal}.
     */
    public void setEinkaufspreis(BigDecimal einkaufspreis) {
        this.einkaufspreis = einkaufspreis;
    }

    /**
     * Gibt den Verkaufspreis des Produkts zurück.
     *
     * @return Der Verkaufspreis als {@link BigDecimal}.
     */
    public BigDecimal getVerkaufspreis() {
        return verkaufspreis;
    }

    /**
     * Setzt den Verkaufspreis des Produkts.
     *
     * @param verkaufspreis Der neue Verkaufspreis als {@link BigDecimal}.
     */
    public void setVerkaufspreis(BigDecimal verkaufspreis) {
        this.verkaufspreis = verkaufspreis;
    }

    /**
     * Gibt den aktuellen Lagerbestand des Produkts zurück.
     *
     * @return Der Lagerbestand als Ganzzahl.
     */
    public int getLagerbestand() {
        return lagerbestand;
    }

    /**
     * Setzt den aktuellen Lagerbestand des Produkts.
     *
     * @param lagerbestand Der neue Lagerbestand als Ganzzahl.
     */
    public void setLagerbestand(int lagerbestand) {
        this.lagerbestand = lagerbestand;
    }

    /**
     * Gibt eine String-Repräsentation des {@code Produktverwaltung}-Objekts zurück.
     * Dies ist nützlich für Debugging- und Logging-Zwecke.
     *
     * @return Eine Zeichenkette, die das Objekt mit all seinen Attributen darstellt.
     */
    @Override
    public String toString() {
        return "Produktverwaltung{" +
                "produktId='" + produktId + '\'' +
                ", produktName='" + produktName + '\'' +
                ", lieferant='" + lieferant + '\'' +
                ", einkaufspreis=" + einkaufspreis +
                ", verkaufspreis=" + verkaufspreis +
                ", lagerbestand=" + lagerbestand +
                '}';
    }
}