package kirschner.flaig.beethoven.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurationsklasse für RabbitMQ-Beans im Beethoven-Service.
 * Definiert Exchanges, Warteschlangen, Bindings, einen JSON-Nachrichtenkonverter
 * und ein {@link RabbitTemplate}, das diesen Konverter verwendet.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Der Name des Exchanges für Nachrichten aus dem E-Commerce-System.
     */
    public static final String ECOMMERCE_EXCHANGE_NAME = "ecommerce.direct.exchange";

    /**
     * Der Name der RabbitMQ-Warteschlange für den Empfang von Statusnachrichten aus dem E-Commerce-System.
     */
    public static final String E_COMMERCE_NACHRICHTEN_WARTESCHLANGE = "ecommerce.status.queue";

    /**
     * Der Routing Key, um E-Commerce-Statusaktualisierungen an die
     * {@link #E_COMMERCE_NACHRICHTEN_WARTESCHLANGE} zu leiten.
     */
    public static final String ECOMMERCE_STATUS_ROUTING_KEY = "ecommerce.status.routingkey";

    /**
     * Der Name des Fanout-Exchanges für Logging-Nachrichten.
     * Nachrichten, die an diesen Exchange gesendet werden, werden an alle gebundenen Queues verteilt.
     */
    public static final String LOGGING_EXCHANGE_NAME = "logging.exchange"; // Angepasst auf "logging"

    /**
     * Der Name der RabbitMQ-Warteschlange für den Empfang von Logging-Nachrichten.
     * Diese Warteschlange hat denselben Namen wie der Exchange, an den sie gebunden ist.
     */
    public static final String LOGGING_QUEUE_NAME = "logging.exchange"; // Warteschlange heißt jetzt wie der Exchange

    /**
     * Erstellt und konfiguriert einen {@link MessageConverter}, der Nachrichten
     * mithilfe von Jackson in das JSON-Format (und zurück) konvertiert.
     *
     * @return Eine Instanz von {@link Jackson2JsonMessageConverter}.
     */
    @Bean
    public MessageConverter jsonNachrichtenKonverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Definiert den Direct Exchange für das E-Commerce-System.
     * @return Eine Instanz von {@link DirectExchange}.
     */
    @Bean
    public DirectExchange ecommerceExchange() {
        return new DirectExchange(ECOMMERCE_EXCHANGE_NAME);
    }

    /**
     * Definiert und erstellt die RabbitMQ-Warteschlange für E-Commerce-Nachrichten.
     * @return Eine Instanz von {@link Queue}.
     */
    @Bean
    public Queue eCommerceWarteschlange() {
        return new Queue(E_COMMERCE_NACHRICHTEN_WARTESCHLANGE, true, false, false);
    }

    /**
     * Erstellt ein Binding zwischen der E-Commerce-Warteschlange und dem E-Commerce-Exchange.
     * @param eCommerceWarteschlange Die zu bindende E-Commerce-Warteschlange.
     * @param ecommerceExchange      Der zu bindende E-Commerce-Exchange.
     * @return Ein {@link Binding}-Objekt.
     */
    @Bean
    public Binding ecommerceBinding(Queue eCommerceWarteschlange, DirectExchange ecommerceExchange) {
        return BindingBuilder.bind(eCommerceWarteschlange)
                .to(ecommerceExchange)
                .with(ECOMMERCE_STATUS_ROUTING_KEY);
    }

    // --- Logging Fanout Exchange und Queue Konfiguration ---

    /**
     * Definiert den Fanout Exchange für Logging-Zwecke.
     * @return Eine Instanz von {@link FanoutExchange}.
     */
    @Bean
    public FanoutExchange loggingFanoutExchange() {
        return new FanoutExchange(LOGGING_EXCHANGE_NAME); // Verwendet den angepassten Exchange-Namen
    }

    /**
     * Definiert und erstellt die RabbitMQ-Warteschlange für Logging-Nachrichten.
     * Der Name der Warteschlange ist identisch mit dem des Logging-Exchanges.
     * @return Eine Instanz von {@link Queue}.
     */
    @Bean
    public Queue loggingWarteschlange() {
        return new Queue(LOGGING_QUEUE_NAME, true, false, false); // Verwendet den angepassten Queue-Namen
    }

    /**
     * Erstellt ein Binding zwischen der Logging-Warteschlange und dem Logging-Fanout-Exchange.
     * @param loggingWarteschlange Die zu bindende Logging-Warteschlange (Name: "logging.exchange").
     * @param loggingFanoutExchange Der zu bindende Logging-Fanout-Exchange (Name: "logging.exchange").
     * @return Ein {@link Binding}-Objekt.
     */
    @Bean
    public Binding loggingBinding(Queue loggingWarteschlange, FanoutExchange loggingFanoutExchange) {
        return BindingBuilder.bind(loggingWarteschlange)
                .to(loggingFanoutExchange);
    }

    // --- RabbitTemplate Konfiguration ---

    /**
     * Erstellt und konfiguriert ein {@link RabbitTemplate} für die Interaktion mit RabbitMQ.
     * @param verbindungsFabrik Die {@link ConnectionFactory}.
     * @return Eine konfigurierte Instanz von {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitVorlage(final ConnectionFactory verbindungsFabrik) {
        final RabbitTemplate vorlage = new RabbitTemplate(verbindungsFabrik);
        vorlage.setMessageConverter(jsonNachrichtenKonverter());
        return vorlage;
    }
}