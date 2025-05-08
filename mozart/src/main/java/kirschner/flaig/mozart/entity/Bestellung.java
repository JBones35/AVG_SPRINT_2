package kirschner.flaig.mozart.entity;

import java.time.LocalDateTime;

/**
 * Repräsentiert eine Bestellung im System.
 * Enthält alle relevanten Informationen zu einer Bestellung, wie Kundendaten, Produktdetails,
 * Bestelldatum, Lieferstatus und Zahlungsmethode.
 */
public class Bestellung {
    /**
     * Die eindeutige Identifikationsnummer der Bestellung.
     */
    private String bestellId;
    /**
     * Die eindeutige Identifikationsnummer des Kunden.
     */
    private String kundenId;
    /**
     * Die E-Mail-Adresse des Kunden.
     */
    private String email;
    /**
     * Die Lieferadresse für die Bestellung.
     */
    private String adresse;
    /**
     * Die eindeutige Identifikationsnummer des bestellten Produkts.
     */
    private String produktId;
    /**
     * Die bestellte Menge des Produkts.
     */
    private int menge;
    /**
     * Das Datum und die Uhrzeit, zu der die Bestellung aufgegeben wurde.
     */
    private LocalDateTime bestelldatum;
    /**
     * Der aktuelle Lieferstatus der Bestellung.
     */
    private DeliveryStatus lieferstatus; // Annahme: DeliveryStatus ist ein Enum oder eine definierte Klasse
    /**
     * Das voraussichtliche oder tatsächliche Lieferdatum der Bestellung.
     */
    private LocalDateTime lieferdatum;
    /**
     * Die für die Bestellung verwendete Zahlungsmethode.
     */
    private String zahlungsmethode;

    /**
     * Konstruktor zum Erstellen einer neuen Bestellungsinstanz.
     *
     * @param bestellId Die ID der Bestellung.
     * @param kundenId Die ID des Kunden.
     * @param email Die E-Mail-Adresse des Kunden.
     * @param adresse Die Lieferadresse.
     * @param produktId Die ID des Produkts.
     * @param menge Die bestellte Menge.
     * @param bestelldatum Das Datum der Bestellung.
     * @param lieferstatus Der Lieferstatus.
     * @param lieferdatum Das Lieferdatum.
     * @param zahlungsmethode Die Zahlungsmethode.
     */
    public Bestellung(String bestellId, String kundenId, String email, String adresse,
                      String produktId, int menge, LocalDateTime bestelldatum, DeliveryStatus lieferstatus,
                      LocalDateTime lieferdatum, String zahlungsmethode) {
        this.bestellId = bestellId;
        this.kundenId = kundenId;
        this.email = email;
        this.adresse = adresse;
        this.produktId = produktId;
        this.menge = menge;
        this.bestelldatum = bestelldatum;
        this.lieferstatus = lieferstatus;
        this.lieferdatum = lieferdatum;
        this.zahlungsmethode = zahlungsmethode;
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
     * Gibt die E-Mail-Adresse zurück.
     *
     * @return Die E-Mail-Adresse des Kunden.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse.
     *
     * @param email Die neue E-Mail-Adresse des Kunden.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt die Lieferadresse zurück.
     *
     * @return Die Lieferadresse.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Setzt die Lieferadresse.
     *
     * @param adresse Die neue Lieferadresse.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Gibt die Produkt-ID zurück.
     *
     * @return Die Identifikationsnummer des Produkts.
     */
    public String getProduktId() {
        return produktId;
    }

    /**
     * Setzt die Produkt-ID.
     *
     * @param produktId Die neue Identifikationsnummer des Produkts.
     */
    public void setProduktId(String produktId) {
        this.produktId = produktId;
    }

    /**
     * Gibt die bestellte Menge zurück.
     *
     * @return Die Menge.
     */
    public int getMenge() {
        return menge;
    }

    /**
     * Setzt die bestellte Menge.
     *
     * @param menge Die neue Menge.
     */
    public void setMenge(int menge) {
        this.menge = menge;
    }

    /**
     * Gibt das Bestelldatum zurück.
     *
     * @return Das Datum und die Uhrzeit der Bestellung.
     */
    public LocalDateTime getBestelldatum() {
        return bestelldatum;
    }

    /**
     * Setzt das Bestelldatum.
     *
     * @param bestelldatum Das neue Datum und die Uhrzeit der Bestellung.
     */
    public void setBestelldatum(LocalDateTime bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    /**
     * Gibt den Lieferstatus zurück.
     *
     * @return Der aktuelle Lieferstatus.
     */
    public DeliveryStatus getLieferstatus() {
        return lieferstatus;
    }

    /**
     * Setzt den Lieferstatus.
     *
     * @param lieferstatus Der neue Lieferstatus.
     */
    public void setLieferstatus(DeliveryStatus lieferstatus) {
        this.lieferstatus = lieferstatus;
    }

    /**
     * Gibt das Lieferdatum zurück.
     *
     * @return Das Datum und die Uhrzeit der Lieferung.
     */
    public LocalDateTime getLieferdatum() {
        return lieferdatum;
    }

    /**
     * Setzt das Lieferdatum.
     *
     * @param lieferdatum Das neue Datum und die Uhrzeit der Lieferung.
     */
    public void setLieferdatum(LocalDateTime lieferdatum) {
        this.lieferdatum = lieferdatum;
    }

    /**
     * Gibt die Zahlungsmethode zurück.
     *
     * @return Die verwendete Zahlungsmethode.
     */
    public String getZahlungsmethode() {
        return zahlungsmethode;
    }

    /**
     * Setzt die Zahlungsmethode.
     *
     * @param zahlungsmethode Die neue Zahlungsmethode.
     */
    public void setZahlungsmethode(String zahlungsmethode) {
        this.zahlungsmethode = zahlungsmethode;
    }

    /**
     * Gibt eine String-Repräsentation des Bestellung-Objekts zurück.
     *
     * @return Eine Zeichenkette, die das Objekt darstellt.
     */
    @Override
    public String toString() {
        return "Bestellung{" +
                "bestellId='" + bestellId + '\'' +
                ", kundenId='" + kundenId + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", produktId='" + produktId + '\'' +
                ", menge=" + menge +
                ", bestelldatum=" + bestelldatum +
                ", lieferstatus=" + lieferstatus +
                ", lieferdatum=" + lieferdatum +
                ", zahlungsmethode='" + zahlungsmethode + '\'' +
                '}';
    }
}