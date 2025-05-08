import { IsString, IsNotEmpty, IsNumber, Min, IsEmail } from 'class-validator';
import { Type } from 'class-transformer';

/**
 * Datenübertragungsobjekt (DTO) für CRM-Bestellinformationen.
 * Dient zur Validierung und Strukturierung von Bestelldaten, die
 * beispielsweise von externen Systemen empfangen werden.
 */
export class BestellungDataSchuhmacher {
  /**
   * Die eindeutige Identifikationsnummer der Bestellung.
   * Muss ein String sein und darf nicht leer sein.
   */
  @IsString()
  @IsNotEmpty()
  bestellId: string;

  /**
   * Die eindeutige Identifikationsnummer des Kunden, der die Bestellung aufgegeben hat.
   * Muss ein String sein und darf nicht leer sein.
   */
  @IsString()
  @IsNotEmpty()
  kundenId: string;

  /**
   * Das Datum der Bestellung.
   * Wird als String erwartet (z.B. im ISO 8601 Format "YYYY-MM-DD").
   * Darf nicht leer sein. In Java entspricht dies oft einem `LocalDate`.
   */
  @IsNotEmpty()
  @IsString() // Hinzugefügt, um den Typ explizit als String zu validieren, passend zur Beschreibung
  bestelldatum: string;

  /**
   * Der Gesamtbetrag der Bestellung.
   * Muss eine Zahl sein und darf nicht negativ sein (Minimum 0).
   * Der `@Type(() => Number)` Decorator hilft `class-transformer`, den Wert
   * korrekt in eine Zahl umzuwandeln, falls er als String im JSON ankommt.
   * In Java entspricht dies oft einem `BigDecimal`. Es ist zu beachten, dass
   * JavaScripts `number`-Typ Präzisionsgrenzen hat, was bei sehr großen oder
   * sehr kleinen Dezimalzahlen relevant sein kann.
   * Darf nicht leer sein.
   */
  @IsNumber()
  @Min(0)
  @Type(() => Number)
  @IsNotEmpty()
  gesamtbetrag: number;

  /**
   * Die E-Mail-Adresse des Kunden.
   * Muss eine gültige E-Mail-Formatierung aufweisen und darf nicht leer sein.
   */
  @IsEmail()
  @IsNotEmpty()
  email: string;

  /**
   * Die Lieferadresse des Kunden.
   * Muss ein String sein und darf nicht leer sein.
   */
  @IsString()
  @IsNotEmpty()
  adressZeichenkette: string;

  /**
   * Der Status der Bestellung.
   * Muss ein String sein und darf nicht leer sein.
   * Für eine präzisere Validierung könnte hier `@IsEnum()` verwendet werden,
   * falls der Status auf eine bestimmte Menge von Werten beschränkt sein soll.
   */
  @IsString()
  @IsNotEmpty()
  status: string;
}
