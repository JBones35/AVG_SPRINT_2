import pika
import sys
import os
import time
from datetime import datetime # War im Original-Code oben nicht importiert

RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', '192.168.178.167')
RABBITMQ_PORT = int(os.getenv('RABBITMQ_PORT', 5672))

RABBITMQ_USER = os.getenv('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.getenv('RABBITMQ_PASS', 'guest')

LOGGING_EXCHANGE = 'logging.exchange'
QUEUE_NAME = 'system-a.log.queue'
ROUTING_KEY = '#'
EXCHANGE_TYPE = 'fanout'
LOG_FILE_NAME = 'received_logs.log'

def main():
    connection = None
    log_file_handle = None # Initialisieren für finally

    try:
        credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
        # Korrigierter Tippfehler: ConnectionParameters statt ConnctionParameters
        parameters = pika.ConnectionParameters(
            host=RABBITMQ_HOST,
            port=RABBITMQ_PORT,
            credentials=credentials
        )
        print(f"Connecting to RabbitMQ at {RABBITMQ_HOST}:{RABBITMQ_PORT}...")
        connection = pika.BlockingConnection(parameters)
        channel = connection.channel()
        print("Connected to RabbitMQ.")

        channel.exchange_declare(exchange=LOGGING_EXCHANGE, exchange_type=EXCHANGE_TYPE, durable=True)
        channel.queue_declare(queue=QUEUE_NAME, durable=True)
        channel.queue_bind(exchange=LOGGING_EXCHANGE, queue=QUEUE_NAME, routing_key=ROUTING_KEY)

        print(f"[*] Öffne Log-Datei zum Anhängen: {LOG_FILE_NAME}")
        log_file_handle = open(LOG_FILE_NAME, 'a', encoding='utf-8')

        def callback(ch, method, properties, body):
            try:
                log_message = body.decode('utf-8')
                timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]
                routing_key = method.routing_key

                print(f" [Konsole] Empfangen: '{log_message}' (RK: {routing_key})")

                log_entry = f"{timestamp} | RK: {routing_key} | {log_message}\n"

                if log_file_handle:
                    log_file_handle.write(log_entry)
                    log_file_handle.flush()

            except Exception as e:
                print(f"[!!!] Fehler im Callback oder beim Schreiben in Datei: {e}")

        channel.basic_consume(queue=QUEUE_NAME, on_message_callback=callback, auto_ack=True)

        print(f' [*] Warte auf Logs in Queue "{QUEUE_NAME}". Schreibe Logs nach "{LOG_FILE_NAME}". Zum Beenden STRG+C drücken.')
        channel.start_consuming()

    except pika.exceptions.AMQPConnectionError as e:
        print(f"[!] Verbindungsfehler: {e}")
        print("[!] Stelle sicher, dass RabbitMQ läuft und Host/Port/Credentials korrekt sind.")
    except pika.exceptions.ProbableAuthenticationError as e:
        print(f"[!] Authentifizierungsfehler: {e}")
        print(f"[!] Überprüfe Benutzername ('{RABBITMQ_USER}') und Passwort.")
    except KeyboardInterrupt:
        print(' [*] Durch Benutzer unterbrochen. Beende...')
    except Exception as e:
         print(f"[!] Ein unerwarteter Fehler ist aufgetreten: {e}")
    finally:
        if log_file_handle is not None:
             print(f"[*] Schließe Log-Datei: {LOG_FILE_NAME}")
             try:
                 log_file_handle.close()
             except Exception as e:
                 print(f"[!] Fehler beim Schließen der Log-Datei: {e}")

        if connection is not None and connection.is_open:
            print("[*] Schließe RabbitMQ-Verbindung.")
            connection.close()
        print("[*] Log-Server gestoppt.")

if __name__ == '__main__':
    try:
        main()
    except Exception as e:
         print(f"[!!!] Kritischer Fehler beim Start: {e}")
         import sys
         sys.exit(1)