package kirschner.flaig.mozart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import kirschner.flaig.mozart.config.RabbitMQConfig;

/**
 * Hauptklasse für die Mozart-Anwendung.
 * Diese Klasse dient als Einstiegspunkt für die Spring Boot Applikation
 * und importiert notwendige Konfigurationen wie {@link RabbitMQConfig}.
 */
@SpringBootApplication
@Import(RabbitMQConfig.class) // Importiert die RabbitMQ-Konfiguration
public class MozartApplication {

    /**
     * Die Hauptmethode, die als Einstiegspunkt für den Start der Mozart-Anwendung dient.
     *
     * @param args Kommandozeilenargumente, die an die Anwendung übergeben werden können.
     */
    public static void main(String[] args) {
        SpringApplication.run(MozartApplication.class, args);
    }

}