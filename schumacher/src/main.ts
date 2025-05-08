import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { RabbitMQLogger } from './logger';
import { ValidationPipe } from '@nestjs/common';
import { Transport, RmqOptions } from '@nestjs/microservices';

/**
 * Startet den NestJS Microservice für die Verarbeitung von RabbitMQ-Nachrichten.
 * Die Funktion initialisiert die Microservice-Instanz mit den notwendigen
 * RabbitMQ-Transportoptionen, konfiguriert einen benutzerdefinierten Logger
 * sowie globale Validierungs-Pipes und startet den Listener.
 */
async function bootstrap() {
  /**
   * Die NestJS Microservice-Anwendungsinstanz.
   * Diese Instanz ist für die Kommunikation mit RabbitMQ konfiguriert.
   * @remarks
   * Die Konfiguration beinhaltet die URL des RabbitMQ-Servers, den Namen der Queue,
   * Optionen für die Queue (z.B. Persistenz) und das Bestätigungsverhalten (noAck).
   */
  const app = await NestFactory.createMicroservice<RmqOptions>(AppModule, {
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://guest:guest@localhost:5672'],
      queue: 'crm.updates.queue',
      routingKey: 'crm.updates.routingkey',
      exchange: 'crm.direct.exchange',
      exchangeType: 'direct',
      queueOptions: {
        durable: false,
      },
      noAck: true,
    },
  });

  app.useLogger(app.get(RabbitMQLogger));
  app.useGlobalPipes(new ValidationPipe({ transform: true }));

  await app.listen();
  console.log('Microservice lauscht auf RabbitMQ Queue: crm.updates.queue');
}

void (async () => {
  await bootstrap();
})();
