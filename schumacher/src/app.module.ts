import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { RabbitMQModule, RabbitMQConfig } from '@golevelup/nestjs-rabbitmq';
import { RabbitMQLogger } from './logger';
import { MockSchuhmacherRepository } from './schumacher/repository/MockSchuhmacherRepository';
import { ECommerceListener } from './schumacher/service/ECommerceListener';

/**
 * Das Hauptmodul der Anwendung.
 * Es konfiguriert und importiert alle notwendigen Module und Provider für die Anwendung,
 * einschließlich der globalen Konfiguration, der RabbitMQ-Anbindung und anwendungsspezifischer
 * Dienste und Repositories.
 *
 * @remarks
 * **Imports:**
 * - `ConfigModule`: Stellt eine globale Konfigurationslösung bereit, die Umgebungsvariablen lädt.
 * - `RabbitMQModule`: Konfiguriert die Verbindung zu RabbitMQ asynchron über eine Factory-Funktion,
 * die Konfigurationswerte aus dem `ConfigService` bezieht. Definiert notwendige Exchanges.
 *
 * **Providers:**
 * - `RabbitMQLogger`: Ein benutzerdefinierter Logger, der Nachrichten an RabbitMQ sendet.
 * - `MockSchuhmacherRepository`: Ein Repository (hier als Mock) für Schuhmacher-Daten.
 * - `ECommerceListener`: Ein Service, der auf E-Commerce-bezogene Nachrichten lauscht.
 *
 * **Exports:**
 * - `RabbitMQLogger`: Macht den Logger für andere Module verfügbar.
 * - `MockSchuhmacherRepository`: Macht das Repository für andere Module verfügbar.
 */
@Module({
  imports: [
    ConfigModule.forRoot({
      /**
       * Gibt an, ob das ConfigModule global registriert werden soll.
       * Wenn true, muss das ConfigModule nicht in jedem Modul einzeln importiert werden.
       */
      isGlobal: true,
    }),
    RabbitMQModule.forRootAsync({
      imports: [ConfigModule],
      /**
       * Eine Factory-Funktion, die die RabbitMQ-Konfiguration dynamisch erstellt.
       * Sie verwendet den `ConfigService`, um notwendige Konfigurationswerte wie die RabbitMQ-URI
       * und den Namen des Log-Exchanges zu laden.
       *
       * @param configService - Der Dienst zum Zugriff auf Konfigurationsvariablen.
       * @returns Ein {@link RabbitMQConfig}-Objekt mit den Verbindungseinstellungen und Exchange-Definitionen.
       * @throws Error - Wirft einen Fehler, wenn die `RABBITMQ_URL` nicht in den Umgebungsvariablen definiert ist.
       */
      useFactory: (configService: ConfigService): RabbitMQConfig => {
        /**
         * Die Verbindungs-URI für den RabbitMQ-Server.
         * Gelesen aus der Umgebungsvariable `RABBITMQ_URL`.
         */
        const uri = configService.get<string>('RABBITMQ_URL');
        if (!uri) {
          throw new Error(
            'RABBITMQ_URL ist nicht in den Umgebungsvariablen definiert. Die Anwendung kann nicht ohne RabbitMQ-URI gestartet werden.',
          );
        }

        /**
         * Der Name des RabbitMQ-Exchanges, der für das Logging verwendet wird.
         * Gelesen aus der Umgebungsvariable `RABBITMQ_LOGS_EXCHANGE`, mit einem Standardwert 'logs_exchange'.
         */
        const logsExchangeName = configService.get<string>(
          'RABBITMQ_LOGS_EXCHANGE',
          'logs_exchange',
        );

        /**
         * Das Konfigurationsobjekt für das RabbitMQ-Modul.
         * Definiert die zu verwendenden Exchanges, die Verbindungs-URI und Optionen für die Initialisierung der Verbindung.
         */
        return {
          exchanges: [
            {
              /**
               * Der Name des Exchanges. Verwendet den Wert von `logsExchangeName`.
               */
              name: logsExchangeName,
              /**
               * Der Typ des Exchanges. 'fanout' leitet Nachrichten an alle gebundenen Queues weiter.
               */
              type: 'fanout',
            },
          ],
          /**
           * Die vollständige Verbindungs-URI für RabbitMQ.
           */
          uri: uri,
          /**
           * Optionen für die Initialisierung der Verbindung.
           * `wait: false` bedeutet, dass die Anwendung nicht auf den Verbindungsaufbau wartet,
           * bevor sie fortfährt (die Verbindung wird im Hintergrund aufgebaut).
           */
          connectionInitOptions: { wait: false },
        };
      },
      inject: [ConfigService],
    }),
  ],
  providers: [RabbitMQLogger, MockSchuhmacherRepository, ECommerceListener],
  exports: [RabbitMQLogger, MockSchuhmacherRepository],
})
export class AppModule {}
