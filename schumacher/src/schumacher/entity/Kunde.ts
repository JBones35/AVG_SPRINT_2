import { Bestellhistorie } from './Bestellhistorie';

/**
 * Definiert die bevorzugten Kontaktmethoden für einen Kunden.
 * Mögliche Werte sind 'EMAIL' oder 'PHONE'.
 */
export type PreferredContactMethod = 'EMAIL' | 'PHONE';

/**
 * Repräsentiert einen Kunden im System.
 * Enthält grundlegende Kundeninformationen sowie eine Historie seiner Bestellungen.
 */
export class Kunde {
  /**
   * Erstellt eine neue Instanz eines Kunden.
   *
   * @param customerId - Die eindeutige Identifikationsnummer des Kunden. Dieses Feld ist nach der Erstellung schreibgeschützt.
   * @param name - Der vollständige Name des Kunden.
   * @param email - Die E-Mail-Adresse des Kunden.
   * @param phone - Die Telefonnummer des Kunden.
   * @param address - Die Postanschrift des Kunden.
   * @param preferredContactMethod - Die vom Kunden bevorzugte Kontaktmethode.
   * @param bestellungen - Eine Liste der Bestellungen des Kunden. Standardmäßig eine leere Liste.
   */
  constructor(
    /**
     * Die eindeutige Identifikationsnummer des Kunden (Primärschlüssel).
     * Dieses Feld ist nach der Initialisierung schreibgeschützt.
     */
    public readonly customerId: string,
    /**
     * Der vollständige Name des Kunden.
     */
    public name: string,
    /**
     * Die E-Mail-Adresse des Kunden.
     */
    public email: string,
    /**
     * Die Telefonnummer des Kunden.
     */
    public phone: string,
    /**
     * Die Postanschrift des Kunden.
     */
    public address: string,
    /**
     * Die vom Kunden bevorzugte Methode zur Kontaktaufnahme.
     * Akzeptiert Werte vom Typ {@link PreferredContactMethod}.
     */
    public preferredContactMethod: PreferredContactMethod,
    /**
     * Eine Liste der bisherigen Bestellungen des Kunden.
     * Jeder Eintrag in der Liste ist eine Instanz der Klasse {@link Bestellhistorie}.
     * Wird standardmäßig als leeres Array initialisiert, falls nicht anders angegeben.
     */
    public bestellungen: Bestellhistorie[] = [],
  ) {}
}
