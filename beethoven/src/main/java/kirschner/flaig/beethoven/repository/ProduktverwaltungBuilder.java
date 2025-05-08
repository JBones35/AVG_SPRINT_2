package kirschner.flaig.beethoven.repository;

import java.math.BigDecimal;
import kirschner.flaig.beethoven.entity.Produktverwaltung;

/**
 * Ein Erbauer (Builder) zur schrittweisen Erstellung von {@link Produktverwaltung}-Objekten.
 * Verwendet das Erbauer-Muster, um die Objekterstellung zu vereinfachen und die Lesbarkeit zu verbessern.
 * Instanzen werden über die statische Fabrikmethode {@link #erhalteInstanz()} bezogen.
 */
public class ProduktverwaltungBuilder {

    /**
     * Die Produkt-ID für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private String produktId;
    /**
     * Der Produktname für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private String produktName;
    /**
     * Der Lieferant für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private String lieferant;
    /**
     * Der Einkaufspreis für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private BigDecimal einkaufspreis;
    /**
     * Der Verkaufspreis für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private BigDecimal verkaufspreis;
    /**
     * Der Lagerbestand für das zu erstellende {@link Produktverwaltung}-Objekt.
     */
    private int lagerbestand;

    /**
     * Privater Konstruktor, um die direkte Instanziierung zu verhindern.
     * Verwenden Sie {@link #erhalteInstanz()}.
     */
    private ProduktverwaltungBuilder() {
    }

    /**
     * Gibt eine neue Instanz des Erbauers zurück.
     * Dies ist die Fabrikmethode für den {@code ProduktverwaltungBuilder}.
     *
     * @return eine neue {@code ProduktverwaltungBuilder}-Instanz.
     */
    public static ProduktverwaltungBuilder erhalteInstanz() {
        return new ProduktverwaltungBuilder();
    }

    /**
     * Setzt die Produkt-ID für das zu erstellende Objekt.
     *
     * @param produktId die eindeutige ID des Produkts.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining (Verkettung von Aufrufen).
     */
    public ProduktverwaltungBuilder mitProduktId(String produktId) {
        this.produktId = produktId;
        return this;
    }

    /**
     * Setzt den Produktnamen für das zu erstellende Objekt.
     *
     * @param produktName der Name des Produkts.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining.
     */
    public ProduktverwaltungBuilder mitProduktName(String produktName) {
        this.produktName = produktName;
        return this;
    }

    /**
     * Setzt den Lieferanten für das zu erstellende Objekt.
     *
     * @param lieferant der Name oder die ID des Lieferanten.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining.
     */
    public ProduktverwaltungBuilder mitLieferant(String lieferant) {
        this.lieferant = lieferant;
        return this;
    }

    /**
     * Setzt den Einkaufspreis für das zu erstellende Objekt.
     *
     * @param einkaufspreis der Einkaufspreis des Produkts.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining.
     */
    public ProduktverwaltungBuilder mitEinkaufspreis(BigDecimal einkaufspreis) {
        this.einkaufspreis = einkaufspreis;
        return this;
    }

    /**
     * Setzt den Verkaufspreis für das zu erstellende Objekt.
     *
     * @param verkaufspreis der Verkaufspreis des Produkts.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining.
     */
    public ProduktverwaltungBuilder mitVerkaufspreis(BigDecimal verkaufspreis) {
        this.verkaufspreis = verkaufspreis;
        return this;
    }

    /**
     * Setzt den Lagerbestand für das zu erstellende Objekt.
     *
     * @param lagerbestand die aktuelle Menge des Produkts auf Lager.
     * @return diese {@code ProduktverwaltungBuilder}-Instanz für Method Chaining.
     */
    public ProduktverwaltungBuilder mitLagerbestand(int lagerbestand) {
        this.lagerbestand = lagerbestand;
        return this;
    }

    /**
     * Erstellt und gibt eine neue Instanz von {@link Produktverwaltung} zurück,
     * basierend auf den zuvor im Erbauer gesetzten Werten.
     * Diese Methode verwendet den öffentlichen Konstruktor der {@link Produktverwaltung}-Klasse,
     * welcher bereits an die deutschen Bezeichner angepasst wurde.
     * <p>
     * Vor dem Konstruktoraufruf könnten hier Validierungen hinzugefügt werden
     * (z.B. Prüfung auf {@code null} für Pflichtfelder).
     *
     * @return eine neue, konfigurierte {@code Produktverwaltung}-Instanz.
     * @throws NullPointerException wenn beispielsweise Pflichtfelder wie {@code produktId} oder {@code produktName}
     * nicht gesetzt wurden und eine entsprechende Validierung implementiert wäre.
     * (Dies dient als beispielhafte Dokumentation einer möglichen Ausnahme.)
     */
    public Produktverwaltung erstellen() {
        // Validierungen könnten hier hinzugefügt werden, z.B.:
        // if (this.produktId == null) {
        // throw new IllegalStateException("Produkt-ID darf nicht null sein.");
        // }
        return new Produktverwaltung(
                this.produktId,
                this.produktName,
                this.lieferant,
                this.einkaufspreis,
                this.verkaufspreis,
                this.lagerbestand
        );
    }
}