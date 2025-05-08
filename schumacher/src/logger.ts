import { Injectable, LoggerService, LogLevel, Optional } from '@nestjs/common';
import { AmqpConnection } from '@golevelup/nestjs-rabbitmq';
import { ConfigService } from '@nestjs/config';

/**
 * Formatiert ein Datumsobjekt in einen Zeitstempel-String im Format YYYY-MM-DD HH:MM:SS.
 * @param date - Das zu formatierende Datumsobjekt.
 * @returns Der formatierte Zeitstempel-String.
 * @remarks
 * Verwendete lokale Variablen:
 * - `year`: Das volle Jahr des Datums.
 * - `month`: Der Monat des Datums (01-12).
 * - `day`: Der Tag des Monats (01-31).
 * - `hours`: Die Stunden des Datums (00-23).
 * - `minutes`: Die Minuten des Datums (00-59).
 * - `seconds`: Die Sekunden des Datums (00-59).
 */
function formatConsoleTimestamp(date: Date): string {
  /**
   * Das volle Jahr des übergebenen Datums.
   */
  const year = date.getFullYear();
  /**
   * Der Monat des übergebenen Datums, auf zwei Stellen formatiert (z.B. '05' für Mai).
   */
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  /**
   * Der Tag des Monats des übergebenen Datums, auf zwei Stellen formatiert.
   */
  const day = date.getDate().toString().padStart(2, '0');
  /**
   * Die Stunden des übergebenen Datums, auf zwei Stellen formatiert.
   */
  const hours = date.getHours().toString().padStart(2, '0');
  /**
   * Die Minuten des übergebenen Datums, auf zwei Stellen formatiert.
   */
  const minutes = date.getMinutes().toString().padStart(2, '0');
  /**
   * Die Sekunden des übergebenen Datums, auf zwei Stellen formatiert.
   */
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * Mappt ein LogLevel-Objekt auf einen für die Konsole lesbaren String.
 * @param level - Das zu mappende LogLevel.
 * @returns Der gemappte LogLevel-String (z.B. 'INFO', 'ERROR').
 */
function mapToConsoleLogLevel(level: LogLevel): string {
  switch (level) {
    case 'log':
      return 'INFO';
    case 'error':
      return 'ERROR';
    case 'warn':
      return 'WARN';
    case 'debug':
      return 'DEBUG';
    case 'verbose':
      return 'VERBOSE';
    default:
      return level ? String(level).toUpperCase() : 'LOG';
  }
}

/**
 * Ein benutzerdefinierter Logger-Service, der Log-Nachrichten sowohl an die Konsole
 * als auch an einen RabbitMQ-Exchange sendet.
 */
@Injectable()
export class RabbitMQLogger implements LoggerService {
  /**
   * Der Name des RabbitMQ-Exchanges, an den die Log-Nachrichten gesendet werden.
   * Wird aus der Konfiguration (`ConfigService`) gelesen.
   */
  private readonly logsExchange: string;
  /**
   * Der Name der Anwendung, der den Log-Nachrichten vorangestellt wird.
   * Wird aus der Konfiguration (`ConfigService`) gelesen.
   */
  private readonly appName: string;

  /**
   * Erstellt eine Instanz des RabbitMQLogger.
   * @param amqpConnection - Die Verbindung zum AMQP-Server (RabbitMQ).
   * @param configService - Der Service zum Zugriff auf Konfigurationsvariablen.
   * @param instanceContext - Optionaler Kontext für die Logger-Instanz,
   * standardmäßig von NestJS bereitgestellt.
   */
  constructor(
    /**
     * Die aktive AMQP-Verbindung für die Kommunikation mit RabbitMQ.
     */
    private readonly amqpConnection: AmqpConnection,
    /**
     * Der Service zum Zugriff auf Umgebungsvariablen und Konfigurationswerte.
     */
    private readonly configService: ConfigService,
    /**
     * Ein optionaler Kontext-String, der oft den Namen der Klasse oder des Moduls angibt,
     * in dem der Logger instanziiert wird.
     */
    @Optional() private instanceContext?: string,
  ) {
    this.logsExchange = this.configService.get<string>(
      'RABBITMQ_LOGS_EXCHANGE',
      'logging.exchange',
    );
    this.appName = this.configService.get<string>(
      'LOG_APP_NAME',
      'SCHUHMACHER',
    );
  }

  /**
   * Formatiert eine Nachricht für die Konsolenausgabe.
   * Objekte werden in einen JSON-String umgewandelt.
   * @param message - Die zu formatierende Nachricht.
   * @returns Die formatierte Nachricht als String.
   */
  private formatConsoleMessage(message: any): string {
    if (typeof message === 'string') {
      return message;
    }
    if (typeof message === 'object' && message !== null) {
      return JSON.stringify(message);
    }
    return String(message);
  }

  /**
   * Verarbeitet eine Log-Nachricht, formatiert sie und sendet sie an RabbitMQ
   * sowie an die Konsole.
   * @param level - Das Log-Level der Nachricht.
   * @param message - Die Log-Nachricht selbst (kann ein beliebiger Typ sein).
   * @param context - Optionaler Kontext-String für die Log-Nachricht.
   * @param trace - Optionaler Stack-Trace (typischerweise für Fehlermeldungen).
   * @remarks
   * Verwendete lokale Variablen:
   * - `effectiveContext`: Der tatsächlich verwendete Kontext für die Log-Nachricht.
   * - `now`: Das aktuelle Datum und die aktuelle Uhrzeit zum Zeitpunkt der Log-Erstellung.
   * - `consoleTimestamp`: Der formatierte Zeitstempel für die Konsolenausgabe.
   * - `consoleLevel`: Das für die Konsole formatierte Log-Level.
   * - `logString`: Die endgültig formatierte Log-Nachricht als String.
   * - `error`: Das Fehlerobjekt im Catch-Block, falls das Senden an RabbitMQ fehlschlägt.
   * - `errorTimestamp`: Der Zeitstempel für die Fehlermeldung beim Fehlschlagen des Sendens an RabbitMQ.
   */
  private async handleLog(
    /**
     * Das spezifische Log-Level dieser Nachricht (z.B. 'log', 'error').
     */
    level: LogLevel,
    /**
     * Der Inhalt der Log-Nachricht.
     */
    message: any,
    /**
     * Ein optionaler Kontext-String, der angibt, woher die Log-Nachricht stammt.
     */
    context?: string,
    /**
     * Ein optionaler Stack-Trace, relevant für Fehlermeldungen.
     */
    trace?: string,
  ) {
    /**
     * Der effektive Kontext für die Log-Nachricht. Verwendet den übergebenen Kontext,
     * den Instanzkontext oder 'Application' als Standard.
     */
    const effectiveContext = context || this.instanceContext || 'Application';
    /**
     * Das aktuelle Datum und die Uhrzeit, zu dem der Log-Eintrag generiert wird.
     */
    const now = new Date();
    /**
     * Der für die Konsolenausgabe formatierte Zeitstempel.
     */
    const consoleTimestamp = formatConsoleTimestamp(now);
    /**
     * Das für die Konsolenausgabe gemappte Log-Level (z.B. INFO, ERROR).
     */
    const consoleLevel = mapToConsoleLogLevel(level);

    /**
     * Die formatierte Log-Zeichenkette, die an die Konsole und RabbitMQ gesendet wird.
     */
    let logString = `[${this.appName}] ${consoleTimestamp} ${consoleLevel} [${effectiveContext}] ${this.formatConsoleMessage(message)}`;
    if (level === 'error' && trace) {
      logString += `\nStackTrace: ${trace}`;
    }

    try {
      await this.amqpConnection.publish(this.logsExchange, '', logString);
    } catch (error) {
      /**
       * Der Zeitstempel, der für die Fehlermeldung verwendet wird, falls das Senden an RabbitMQ fehlschlägt.
       */
      const errorTimestamp = formatConsoleTimestamp(new Date());
      console.error(
        `[${this.appName}] ${errorTimestamp} ERROR [RabbitMQLogger] Fehler beim Senden des '${consoleLevel}'-Levels an RabbitMQ: ${error instanceof Error ? error.message : String(error)}`,
      );
    }

    switch (level) {
      case 'log':
        console.log(logString);
        break;
      case 'error':
        console.error(logString);
        break;
      case 'warn':
        console.warn(logString);
        break;
      case 'debug':
        console.debug(logString);
        break;
      case 'verbose':
        console.debug(logString); // Standardmäßig auf console.debug für verbose
        break;
      default:
        console.log(logString);
        break;
    }
  }

  /**
   * Loggt eine Standard-Nachricht (LogLevel 'log').
   * @param message - Die zu loggende Nachricht.
   * @param context - Optionaler Kontext für die Log-Nachricht.
   */
  async log(message: any, context?: string) {
    await this.handleLog('log', message, context);
  }

  /**
   * Loggt eine Fehlermeldung (LogLevel 'error').
   * @param message - Die zu loggende Fehlermeldung.
   * @param trace - Optionaler Stack-Trace des Fehlers.
   * @param context - Optionaler Kontext für die Log-Nachricht.
   */
  async error(message: any, trace?: string, context?: string) {
    await this.handleLog('error', message, context, trace);
  }

  /**
   * Loggt eine Warnung (LogLevel 'warn').
   * @param message - Die zu loggende Warnung.
   * @param context - Optionaler Kontext für die Log-Nachricht.
   */
  async warn(message: any, context?: string) {
    await this.handleLog('warn', message, context);
  }

  /**
   * Loggt eine Debug-Nachricht (LogLevel 'debug').
   * @param message - Die zu loggende Debug-Nachricht.
   * @param context - Optionaler Kontext für die Log-Nachricht.
   */
  async debug?(message: any, context?: string) {
    await this.handleLog('debug', message, context);
  }

  /**
   * Loggt eine ausführliche Nachricht (LogLevel 'verbose').
   * @param message - Die zu loggende ausführliche Nachricht.
   * @param context - Optionaler Kontext für die Log-Nachricht.
   */
  async verbose?(message: any, context?: string) {
    await this.handleLog('verbose', message, context);
  }
}
