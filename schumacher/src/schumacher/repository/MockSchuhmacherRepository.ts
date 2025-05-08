import { Injectable, NotFoundException } from '@nestjs/common';
import { Bestellhistorie } from '../entity/Bestellhistorie';
import { Kunde } from '../entity/Kunde';

/**
 * Ein Mock-Repository für Schuhmacher-Daten (Kunden und Bestellhistorien).
 * Diese Klasse simuliert eine Datenquelle im Speicher für Test- und Entwicklungszwecke.
 * Sie ist als `@Injectable()` Service in einem NestJS-Kontext konzipiert.
 */
@Injectable()
export class MockSchuhmacherRepository {
  /**
   * Interne Speicherung für Mock-Bestellhistorie-Daten.
   * Diese Liste wird im Konstruktor initialisiert.
   */
  private readonly bestellhistorieData: Bestellhistorie[];
  /**
   * Interne Speicherung für Mock-Kundendaten.
   * Diese Liste wird im Konstruktor initialisiert.
   */
  private readonly kundenData: Kunde[];

  /**
   * Initialisiert das Mock-Repository mit einem Satz von Beispieldaten
   * für Bestellhistorien und Kunden.
   * @remarks
   * Lokale Konstante `initialBestellhistorie`: Enthält die ersten Mock-Bestelldaten,
   * die sowohl für die globale Bestellliste als auch für die Bestellungen
   * der initialen Kunden verwendet werden.
   */
  constructor() {
    /**
     * Eine temporäre Liste von Bestellhistorie-Objekten, die zur Initialisierung
     * der `bestellhistorieData` und der Bestellungen der initialen `kundenData` verwendet wird.
     */
    const initialBestellhistorie: Bestellhistorie[] = [
      new Bestellhistorie(
        'ORD-MOCK-2025-001',
        new Date('2023-01-01'),
        100.0,
        2,
      ),
      new Bestellhistorie(
        'ORD-MOCK-2025-002',
        new Date('2023-02-01'),
        200.0,
        1,
      ),
    ];

    this.bestellhistorieData = [...initialBestellhistorie];

    this.kundenData = [
      new Kunde(
        'CUST-MOCK-SALZBURG',
        'Jonas Kirschner',
        'info@mozarteum.at',
        '0664 1234567',
        'Mozarteum Salzburg, Schwarzstraße 26, 5020 Salzburg',
        'EMAIL',
        [initialBestellhistorie[0]],
      ),
      new Kunde(
        'CUST-MOCK-WIEN',
        'David Flaig',
        'info@mozarteum.at',
        '0664 1234567',
        'Mozarteum Salzburg, Schwarzstraße 26, 5020 Salzburg',
        'EMAIL',
        [initialBestellhistorie[0]],
      ),
    ];
  }

  /**
   * Gibt eine Kopie aller im Repository gespeicherten Bestellhistorie-Einträge zurück.
   * @returns Ein Array von {@link Bestellhistorie}-Objekten.
   * Die Rückgabe ist eine Kopie, um externe Modifikationen der internen Daten zu verhindern.
   */
  public getBestellhistorie(): Bestellhistorie[] {
    return [...this.bestellhistorieData];
  }

  /**
   * Fügt einen neuen Bestellhistorie-Eintrag zum Repository hinzu.
   * @param bestellung - Das {@link Bestellhistorie}-Objekt, das hinzugefügt werden soll.
   */
  public addBestellung(bestellung: Bestellhistorie): void {
    this.bestellhistorieData.push(bestellung);
  }

  /**
   * Gibt eine Kopie aller im Repository gespeicherten Kunden zurück.
   * @returns Ein Array von {@link Kunde}-Objekten.
   * Die Rückgabe ist eine Kopie, um externe Modifikationen der internen Daten zu verhindern.
   */
  public getKunden(): Kunde[] {
    return [...this.kundenData];
  }

  /**
   * Fügt einen neuen Kunden zum Repository hinzu.
   * @param kunde - Das {@link Kunde}-Objekt, das hinzugefügt werden soll.
   */
  public addKunde(kunde: Kunde): void {
    this.kundenData.push(kunde);
  }

  /**
   * Findet einen Kunden anhand seiner ID.
   * @param kundenId - Die ID des zu suchenden Kunden.
   * @returns Das gefundene {@link Kunde}-Objekt oder `undefined`, wenn kein Kunde mit der gegebenen ID existiert.
   * @remarks
   * Lokale Konstante `kunde`: Speichert das Ergebnis der Suche innerhalb der Methode.
   */
  public findKundeById(kundenId: string): Kunde | undefined {
    /**
     * Das Ergebnis der Suche nach einem Kunden mit der spezifizierten `kundenId`.
     * Ist `undefined`, falls kein passender Kunde gefunden wird.
     */
    const kunde = this.kundenData.find((k) => k.customerId === kundenId);
    return kunde;
  }

  /**
   * Findet alle Bestellungen eines spezifischen Kunden anhand seiner ID.
   * @param kundenId - Die ID des Kunden, dessen Bestellungen gesucht werden.
   * @returns Ein Array von {@link Bestellhistorie}-Objekten des Kunden. Gibt ein leeres Array zurück,
   * wenn der Kunde nicht gefunden wird oder keine Bestellungen hat.
   * Die Rückgabe ist eine Kopie der Bestellliste des Kunden.
   * @remarks
   * Lokale Konstante `kunde`: Speichert das Ergebnis der Kundensuche.
   */
  public findBestellungenByKundenId(kundenId: string): Bestellhistorie[] {
    /**
     * Der Kunde, der anhand der `kundenId` gesucht wird.
     */
    const kunde = this.kundenData.find((k) => k.customerId === kundenId);
    if (kunde && kunde.bestellungen) {
      return [...kunde.bestellungen];
    }
    return [];
  }

  /**
   * Fügt eine Bestellung zur Bestellhistorie eines spezifischen Kunden hinzu.
   * Die Bestellung wird auch zur globalen Liste aller Bestellhistorien hinzugefügt,
   * falls sie dort noch nicht existiert.
   * @param kundenId - Die ID des Kunden, zu dem die Bestellung hinzugefügt werden soll.
   * @param bestellung - Das {@link Bestellhistorie}-Objekt, das hinzugefügt werden soll.
   * @returns Das aktualisierte {@link Kunde}-Objekt mit der hinzugefügten Bestellung.
   * @throws {@link NotFoundException} - Wenn kein Kunde mit der angegebenen `kundenId` gefunden wird.
   * @remarks
   * Lokale Konstante `kunde`: Speichert den gefundenen Kunden.
   */
  public addBestellungZuKunde(
    kundenId: string,
    bestellung: Bestellhistorie,
  ): Kunde {
    /**
     * Der Kunde, der anhand der `kundenId` gesucht und dessen Bestellhistorie aktualisiert wird.
     */
    const kunde = this.findKundeById(kundenId);
    if (kunde) {
      if (
        !this.bestellhistorieData.find((b) => b.orderId === bestellung.orderId)
      ) {
        this.addBestellung(bestellung);
      }
      kunde.bestellungen.push(bestellung);
      return kunde;
    }
    throw new NotFoundException(
      `Kunde mit ID ${kundenId} nicht gefunden, um Bestellung hinzuzufügen.`,
    );
  }
}
