package kirschner.flaig.mozart.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Repräsentiert Bestelldaten, die spezifisch für den "Schuhmacher"-Kontext sind oder von diesem System stammen.
 * Diese Klasse dient als Datenübertragungsobjekt für Bestellinformationen.
 */
public class BestellungDataSchuhmacher {
    /**
     * Die eindeutige Identifikationsnummer der Bestellung.
     */
    private String bestellId;
    /**
     * Die eindeutige Identifikationsnummer des Kunden.
     */
    private String kundenId;
    /**
     * Das Datum, an dem die Bestellung aufgegeben wurde.
     */
    private LocalDate bestelldatum;
    /**
     * Der Gesamtbetrag der Bestellung.
     */
    private BigDecimal gesamtbetrag;
    /**
     * Der aktuelle Status der Bestellung.
     */
    private String status;
    /**
     * Die E-Mail-Adresse des Kunden.
     */
    private String email;
    /**
     * Die vollständige Adresse als Zeichenkette.
     */
    private String adressZeichenkette;

    /**
     * Standardkonstruktor.
     * Erforderlich für bestimmte Frameworks und Bibliotheken zur Instanziierung.
     */
    public BestellungDataSchuhmacher() {
    }

    /**
     * Konstruktor zum Erstellen einer neuen {@code BestellungDataSchuhmacher}-Instanz mit allen Feldern.
     *
     * @param bestellId Die ID der Bestellung.
     * @param kundenId Die ID des Kunden.
     * @param bestelldatum Das Datum der Bestellung.
     * @param gesamtbetrag Der Gesamtbetrag der Bestellung.
     * @param status Der Status der Bestellung.
     * @param email Die E-Mail-Adresse des Kunden.
     * @param adressZeichenkette Die Adresse als formatierte Zeichenkette.
     */
    public BestellungDataSchuhmacher(String bestellId, String kundenId, LocalDate bestelldatum, BigDecimal gesamtbetrag, String status, String email, String adressZeichenkette) {
        this.bestellId = bestellId;
        this.kundenId = kundenId;
        this.email = email;
        this.adressZeichenkette = adressZeichenkette;
        this.bestelldatum = bestelldatum;
        this.gesamtbetrag = gesamtbetrag;
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
     * Gibt die Kunden-ID zurück.
     *
     * @return Die Identifikationsnummer des Kunden.
     */
    public String getKundenId() {
        return kundenId;
    }

    /**
     * Setzt die Kunden-ID.
     *
     * @param kundenId Die neue Identifikationsnummer des Kunden.
     */
    public void setKundenId(String kundenId) {
        this.kundenId = kundenId;
    }

    /**
     * Gibt das Bestelldatum zurück.
     *
     * @return Das Datum der Bestellung.
     */
    public LocalDate getBestelldatum() {
        return bestelldatum;
    }

    /**
     * Setzt das Bestelldatum.
     *
     * @param bestelldatum Das neue Datum der Bestellung.
     */
    public void setBestelldatum(LocalDate bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    /**
     * Gibt den Gesamtbetrag der Bestellung zurück.
     *
     * @return Der Gesamtbetrag.
     */
    public BigDecimal getGesamtbetrag() {
        return gesamtbetrag;
    }

    /**
     * Setzt den Gesamtbetrag der Bestellung.
     *
     * @param gesamtbetrag Der neue Gesamtbetrag.
     */
    public void setGesamtbetrag(BigDecimal gesamtbetrag) {
        this.gesamtbetrag = gesamtbetrag;
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
     * Gibt die E-Mail-Adresse des Kunden zurück.
     *
     * @return Die E-Mail-Adresse.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse des Kunden.
     *
     * @param email Die neue E-Mail-Adresse.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt die Adresse als Zeichenkette zurück.
     *
     * @return Die formatierte Adresszeichenkette.
     */
    public String getAdressZeichenkette() {
        return adressZeichenkette;
    }

    /**
     * Setzt die Adresse als Zeichenkette.
     *
     * @param adressZeichenkette Die neue formatierte Adresszeichenkette.
     */
    public void setAdressZeichenkette(String adressZeichenkette) {
        this.adressZeichenkette = adressZeichenkette;
    }

    /**
     * Gibt eine String-Repräsentation des {@code BestellungDataSchuhmacher}-Objekts zurück.
     *
     * @return Eine Zeichenkette, die das Objekt darstellt.
     */
    @Override
    public String toString() {
        return "BestellungDataSchuhmacher{" +
                "bestellId='" + bestellId + '\'' +
                ", kundenId='" + kundenId + '\'' +
                ", bestelldatum=" + bestelldatum +
                ", gesamtbetrag=" + gesamtbetrag +
                ", status='" + status + '\'' +
                ", email='" + email + '\'' +
                ", adressZeichenkette='" + adressZeichenkette + '\'' +
                '}';
    }
}