/**
 * Repräsentiert einen Eintrag in der Bestellhistorie eines Kunden.
 * Enthält Details zu einer einzelnen Bestellung.
 */
export class Bestellhistorie {
  /**
   * Erstellt eine neue Instanz eines Bestellhistorie-Eintrags.
   *
   * @param orderId - Die eindeutige Identifikationsnummer der Bestellung. Dies ist der Primärschlüssel und kann nach der Erstellung nicht mehr geändert werden.
   * @param orderDate - Das Datum, an dem die Bestellung aufgegeben wurde.
   * @param totalAmount - Der Gesamtbetrag der Bestellung. Für Dezimalwerte wird hier der Typ `number` verwendet; alternativ könnte eine BigDecimal-Bibliothek für höhere Präzision genutzt werden.
   * @param status - Der aktuelle Status der Bestellung, repräsentiert durch eine Zahl (z.B. Enum-Wert).
   */
  constructor(
    /**
     * Die eindeutige Identifikationsnummer der Bestellung (Primärschlüssel).
     * Dieses Feld ist schreibgeschützt nach der Initialisierung.
     */
    public readonly orderId: string,
    /**
     * Das Datum, an dem die Bestellung aufgegeben wurde.
     */
    public orderDate: Date,
    /**
     * Der Gesamtbetrag der Bestellung.
     * Es wird empfohlen, für Währungsbeträge gegebenenfalls eine genauere Darstellung als `number` zu verwenden (z.B. BigDecimal),
     * um Rundungsfehler zu vermeiden.
     */
    public totalAmount: number,
    /**
     * Der Status der Bestellung, typischerweise als numerischer Wert eines Enums abgebildet
     * (z.B. 0 für 'Ausstehend', 1 für 'Versandt', 2 für 'Abgeschlossen').
     */
    public status: number,
  ) {}
}
