package kirschner.flaig.beethoven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import kirschner.flaig.beethoven.config.RabbitMQConfig;

/**
 * Hauptklasse für die Beethoven-Anwendung.
 * Diese Klasse dient als Einstiegspunkt für die Spring Boot Applikation
 * und importiert notwendige Konfigurationen wie {@link RabbitMQConfig}
 * für den Beethoven-Service.
 */
@SpringBootApplication
@Import(RabbitMQConfig.class) // Importiert die RabbitMQ-Konfiguration für Beethoven
public class BeethovenApplication {

    /**
     * Die Hauptmethode, die als Einstiegspunkt für den Start der Beethoven-Anwendung dient.
     *
     * @param args Kommandozeilenargumente, die an die Anwendung übergeben werden können.
     */
    public static void main(String[] args) {
        SpringApplication.run(BeethovenApplication.class, args);
    }

}