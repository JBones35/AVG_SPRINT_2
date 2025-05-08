/* eslint-disable @typescript-eslint/no-unused-vars */
import { RabbitSubscribe, Nack } from '@golevelup/nestjs-rabbitmq';
import { Controller, Logger } from '@nestjs/common';
import { BestellungDataSchuhmacher } from '../entity/CrmOrderDTO';
import { Bestellhistorie } from '../entity/Bestellhistorie';
import { MockSchuhmacherRepository } from '../repository/MockSchuhmacherRepository';
import { Kunde } from '../entity/Kunde';
import { Ctx, Payload, RmqContext } from '@nestjs/microservices';

/**
 * Ein Listener-Service, der auf Nachrichten aus einem E-Commerce-System lauscht,
 * die über RabbitMQ empfangen werden. Er verarbeitet Aktualisierungen von CRM-Bestelldaten.
 */
@Controller()
export class ECommerceListener {
  /**
   * Eine schreibgeschützte Logger-Instanz für diese Klasse, um Ereignisse und Fehler zu protokollieren.
   * Der Kontext des Loggers ist auf den Namen der `ECommerceListener`-Klasse gesetzt.
   */
  private readonly LOGGER = new Logger(ECommerceListener.name);

  /**
   * Erstellt eine Instanz des `ECommerceListener`.
   * @param repo - Das {@link MockSchuhmacherRepository}, das für den Datenzugriff
   * und die Datenmanipulation verwendet wird. Wird per Dependency Injection bereitgestellt.
   */
  constructor(
    /**
     * Das Repository für den Zugriff auf Kunden- und Bestelldaten.
     */
    private readonly repo: MockSchuhmacherRepository,
  ) {}

  /**
   * Verarbeitet eingehende CRM-Update-Nachrichten von der RabbitMQ-Queue 'crm.updates.queue'.
   * Diese Methode ist sowohl als RabbitMQ-Abonnent als auch als NestJS Event-Pattern-Handler konfiguriert.
   * Sie validiert die Nachricht, legt bei Bedarf neue Kunden oder Bestellungen an
   * und aktualisiert bestehende Bestellungen.
   *
   * @param dto - Das {@link BestellungDataSchuhmacher} Datenübertragungsobjekt, das die Bestelldaten enthält.
   * Kann `null` oder `undefined` sein, wenn die Nachricht fehlerhaft ist.
   * @returns `void` bei erfolgreicher Verarbeitung oder ein {@link Nack}-Objekt,
   * um die Nachricht abzulehnen (und optional erneut zustellen zu lassen), falls ein Fehler auftritt
   * oder die Nachricht ungültig ist.
   * @remarks
   * Lokale Variablen:
   * - `kunde`: Das Kundenobjekt, das entweder gefunden oder neu erstellt wird.
   * - `neuerKunde`: Ein temporäres Objekt für einen neu zu erstellenden Kunden.
   * - `bestellung`: Das Bestellhistorie-Objekt, das entweder gefunden oder neu erstellt wird.
   * - `orderDate`: Das aus dem DTO geparste Bestelldatum für neue Bestellungen.
   * - `neueBestellung`: Ein temporäres Objekt für eine neu zu erstellende Bestellung.
   * - `orderDateFromDTO`: Das aus dem DTO geparste Bestelldatum für Aktualisierungen.
   * - `error`: Das Fehlerobjekt im catch-Block.
   */
  @RabbitSubscribe({
    queue: 'crm.updates.queue',
    routingKey: 'crm.updates.routingkey',
    exchange: 'crm.direct.exchange',
    queueOptions: {
      durable: false,
    },
  })
  public handleCRMUpdate(
    @Payload() dto: BestellungDataSchuhmacher | null | undefined,
    @Ctx() context: RmqContext,
  ): void | Nack {
    if (
      !dto ||
      typeof dto !== 'object' ||
      !dto.kundenId ||
      !dto.bestellId ||
      !dto.status
    ) {
      this.LOGGER.error(
        `Ungültige oder unvollständige Nachricht empfangen: ${JSON.stringify(
          dto,
        )}`,
      );
      return new Nack(false);
    }

    this.LOGGER.debug(
      `Received message from E-Commerce System: ${JSON.stringify(dto)}`,
    );

    /**
     * Der Kunde, der anhand der `customerId` aus dem DTO im Repository gesucht wird.
     * Kann `undefined` sein, wenn kein Kunde gefunden wird.
     */
    let kunde = this.repo.findKundeById(dto.kundenId);

    if (!kunde) {
      /**
       * Ein neues Kundenobjekt, das erstellt wird, falls kein bestehender Kunde
       * mit der `customerId` aus dem DTO gefunden wurde.
       */
      const neuerKunde = new Kunde(
        dto.kundenId,
        'Unknown Name', // Standardwert, da DTO keinen Namen enthält
        dto.bestellId,
        'Unknown Phone', // Standardwert, da DTO kein Telefon enthält
        dto.adressZeichenkette,
        'EMAIL', // Standardwert
        [],
      );
      this.repo.addKunde(neuerKunde);
      kunde = neuerKunde;
      this.LOGGER.debug(`New customer added with ID: ${dto.kundenId}`);
    }

    /**
     * Die Bestellung des Kunden, die anhand der `orderId` aus dem DTO gesucht wird.
     * Kann `undefined` sein, wenn keine passende Bestellung gefunden wird.
     */
    let bestellung = kunde.bestellungen.find(
      (b) => b.orderId === dto.bestellId,
    );

    if (!bestellung) {
      /**
       * Das aus dem `dto.orderDate`-String geparste Datumsobjekt für eine neue Bestellung.
       */
      const orderDate = new Date(dto.bestelldatum);
      if (isNaN(orderDate.getTime())) {
        this.LOGGER.error(
          `Ungültiges OrderDate Format in neuer Bestellung: ${dto.bestelldatum} für Bestellung ${dto.bestellId}`,
        );
        return new Nack(false);
      }

      /**
       * Ein neues Bestellhistorie-Objekt, das erstellt wird, falls keine bestehende Bestellung
       * mit der `orderId` aus dem DTO für den Kunden gefunden wurde.
       * Der Status wird initial auf 1 (z.B. 'PROCESSING') gesetzt.
       */
      const neueBestellung = new Bestellhistorie(
        dto.bestellId,
        orderDate,
        dto.gesamtbetrag,
        1, // Initialer Status, wird später durch mapDTOStatusToBestellungStatus überschrieben
      );

      this.repo.addBestellungZuKunde(kunde.customerId, neueBestellung);
      bestellung = neueBestellung;
      this.LOGGER.debug(
        `New order ${dto.bestellId} added for customer ${kunde.customerId}. Initial status from DTO will be applied.`,
      );
    }

    try {
      this.mapDTOStatusToBestellungStatus(dto, bestellung);

      /**
       * Das aus dem `dto.orderDate`-String geparste Datumsobjekt für die Aktualisierung einer Bestellung.
       */
      const orderDateFromDTO = new Date(dto.bestelldatum);
      if (isNaN(orderDateFromDTO.getTime())) {
        this.LOGGER.error(
          `Ungültiges OrderDate Format beim Update: ${dto.bestelldatum} für Bestellung ${dto.bestellId}`,
        );
        return new Nack(false);
      }
      bestellung.orderDate = orderDateFromDTO;
      bestellung.totalAmount = dto.gesamtbetrag;

      this.LOGGER.debug(
        `Bestellung ${dto.bestellId} status updated to ${bestellung.status}`,
      );
      this.LOGGER.debug(
        `Bestellung ${dto.bestellId} updated with orderDate: ${bestellung.orderDate.toISOString()}, totalAmount: ${bestellung.totalAmount}`,
      );
    } catch (error: any) {
      /**
       * Das Fehlerobjekt, das während des Versuchs, den Bestellstatus zu mappen
       * oder die Bestelldaten zu aktualisieren, abgefangen wurde.
       */
      if (error instanceof Error) {
        this.LOGGER.error(
          `Fehler bei der Statusaktualisierung für Bestellung ${dto.bestellId}: ${error.message}`,
          error.stack,
        );
      } else {
        this.LOGGER.error(
          `Unbekannter Fehler bei der Statusaktualisierung für Bestellung ${dto.bestellId}: ${JSON.stringify(
            error,
          )}`,
        );
      }
      return new Nack(false);
    }
  }

  /**
   * Mappt den Statusstring aus dem {@link BestellungDataSchuhmacher} auf einen numerischen Status
   * des {@link Bestellhistorie}-Objekts.
   * Wirft einen Fehler, wenn der Status unbekannt, `null`, `undefined` oder leer ist.
   *
   * @param dto - Das {@link BestellungDataSchuhmacher}, das den zu mappenden Status enthält.
   * @param bestellung - Das {@link Bestellhistorie}-Objekt, dessen Status aktualisiert wird.
   * @throws {@link Error} - Wenn der DTO-Status ungültig oder nicht mapbar ist.
   */
  private mapDTOStatusToBestellungStatus(
    dto: BestellungDataSchuhmacher,
    bestellung: Bestellhistorie,
  ): void {
    switch (dto.status.toUpperCase()) {
      case 'SHIPPED':
        bestellung.status = 2; // Angenommen: 2 entspricht 'Versandt' oder 'Abgeschlossen'
        break;
      case 'DELIVERED':
        bestellung.status = 2; // Angenommen: 2 entspricht 'Geliefert' oder 'Abgeschlossen'
        break;
      case 'PROCESSING':
        bestellung.status = 1; // Angenommen: 1 entspricht 'In Bearbeitung'
        break;
      case null:
      case undefined:
      case '':
        this.LOGGER.error(
          `Status ist leer oder nicht vorhanden für Bestellung ${dto.bestellId}. DTO-Status: '${dto.status}'`,
        );
        throw new Error(
          `Status ist leer oder nicht vorhanden für Bestellung ${dto.bestellId}`,
        );
      default:
        this.LOGGER.error(
          `Unbekannter Status '${dto.status}' für Bestellung ${dto.bestellId}`,
        );
        throw new Error(
          `Unbekannter Status '${dto.status}' für Bestellung ${dto.bestellId}`,
        );
    }
  }
}
