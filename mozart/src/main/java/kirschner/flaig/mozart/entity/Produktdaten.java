package kirschner.flaig.mozart.entity;

import java.math.BigDecimal;

/**
 * Repräsentiert die Stammdaten eines Produkts.
 * Diese Klasse enthält detaillierte Informationen wie Produktidentifikation, Name,
 * Kategorie, Preis und Lagermenge.
 */
public class Produktdaten {

    /**
     * Die eindeutige Identifikationsnummer des Produkts.
     */
    private String produktId;

    /**
     * Der Name des Produkts.
     */
    private String produktName;

    /**
     * Die Kategorie, zu der das Produkt gehört.
     */
    private String kategorie;

    /**
     * Der Preis des Produkts.
     */
    private BigDecimal preis;

    /**
     * Die aktuell im Lager verfügbare Menge des Produkts.
     */
    private Integer lagermenge;

    /**
     * Standardkonstruktor.
     * Erstellt eine leere Instanz von {@code Produktdaten}.
     * Wird oft von Frameworks für die Objektinstanziierung benötigt.
     */
    public Produktdaten() {
    }

    /**
     * Konstruktor zur Erstellung einer neuen Instanz von {@code Produktdaten} mit allen Attributen.
     *
     * @param produktId Die eindeutige ID des Produkts.
     * @param produktName Der Name des Produkts.
     * @param kategorie Die Kategorie des Produkts.
     * @param preis Der Preis des Produkts.
     * @param lagermenge Die aktuelle Lagermenge des Produkts.
     */
    public Produktdaten(String produktId, String produktName, String kategorie,
                        BigDecimal preis, Integer lagermenge) {
        this.produktId = produktId;
        this.produktName = produktName;
        this.kategorie = kategorie;
        this.preis = preis;
        this.lagermenge = lagermenge;
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
     * Gibt die Kategorie des Produkts zurück.
     *
     * @return Die Produktkategorie.
     */
    public String getKategorie() {
        return kategorie;
    }

    /**
     * Setzt die Kategorie des Produkts.
     *
     * @param kategorie Die neue Produktkategorie.
     */
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    /**
     * Gibt den Preis des Produkts zurück.
     *
     * @return Der Produktpreis als {@link BigDecimal}.
     */
    public BigDecimal getPreis() {
        return preis;
    }

    /**
     * Setzt den Preis des Produkts.
     *
     * @param preis Der neue Produktpreis als {@link BigDecimal}.
     */
    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    /**
     * Gibt die aktuelle Lagermenge des Produkts zurück.
     *
     * @return Die Lagermenge als {@link Integer}.
     */
    public Integer getLagermenge() {
        return lagermenge;
    }

    /**
     * Setzt die aktuelle Lagermenge des Produkts.
     *
     * @param lagermenge Die neue Lagermenge als {@link Integer}.
     */
    public void setLagermenge(Integer lagermenge) {
        this.lagermenge = lagermenge;
    }

    /**
     * Gibt eine String-Repräsentation des {@code Produktdaten}-Objekts zurück.
     * Dies ist nützlich für Debugging- und Logging-Zwecke.
     *
     * @return Eine Zeichenkette, die das Objekt mit all seinen Attributen darstellt.
     */
    @Override
    public String toString() {
        return "Produktdaten{" +
                "produktId='" + produktId + '\'' +
                ", produktName='" + produktName + '\'' +
                ", kategorie='" + kategorie + '\'' +
                ", preis=" + preis +
                ", lagermenge=" + lagermenge +
                '}';
    }
}