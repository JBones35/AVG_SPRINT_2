package kirschner.flaig.beethoven.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurationsklasse für RabbitMQ-Beans im Beethoven-Service.
 * Definiert einen Exchange, eine Warteschlange für E-Commerce-Statusnachrichten,
 * ein Binding, einen JSON-Nachrichtenkonverter und ein {@link RabbitTemplate},
 * das diesen Konverter verwendet.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Der Name des Exchanges für Nachrichten aus dem E-Commerce-System.
     * An diesen Exchange werden Nachrichten gesendet, die dann per Routing Key
     * an die entsprechenden Queues weitergeleitet werden.
     */
    public static final String ECOMMERCE_EXCHANGE_NAME = "ecommerce.direct.exchange"; // Konsistent mit anderer Config

    /**
     * Der Name der RabbitMQ-Warteschlange für den Empfang von Statusnachrichten aus dem E-Commerce-System.
     */
    public static final String E_COMMERCE_NACHRICHTEN_WARTESCHLANGE = "ecommerce.status.queue";

    /**
     * Der Routing Key, um E-Commerce-Statusaktualisierungen an die
     * {@link #E_COMMERCE_NACHRICHTEN_WARTESCHLANGE} zu leiten.
     */
    public static final String ECOMMERCE_STATUS_ROUTING_KEY = "ecommerce.status.routingkey"; // Konsistent mit anderer Config

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
     * Nachrichten, die an diesen Exchange mit dem passenden Routing Key gesendet werden,
     * werden an die gebundene Queue weitergeleitet.
     * @return Eine Instanz von {@link DirectExchange}.
     */
    @Bean
    public DirectExchange ecommerceExchange() {
        return new DirectExchange(ECOMMERCE_EXCHANGE_NAME);
    }

    /**
     * Definiert und erstellt die RabbitMQ-Warteschlange für E-Commerce-Nachrichten.
     * Die Warteschlange ist als durebelfähig (persistent) konfiguriert,
     * d.h., Nachrichten überleben einen Neustart des Brokers.
     *
     * @return Eine Instanz von {@link Queue}, die die E-Commerce-Warteschlange repräsentiert.
     */
    @Bean
    public Queue eCommerceWarteschlange() {
        // Parameter: name, durable, exclusive, autoDelete
        return new Queue(E_COMMERCE_NACHRICHTEN_WARTESCHLANGE, true, false, false);
    }

    /**
     * Erstellt ein Binding zwischen der E-Commerce-Warteschlange und dem E-Commerce-Exchange
     * unter Verwendung des definierten E-Commerce-Status-Routing-Keys.
     * Dies stellt sicher, dass Nachrichten, die an den {@link #ECOMMERCE_EXCHANGE_NAME}
     * mit dem Routing Key {@link #ECOMMERCE_STATUS_ROUTING_KEY} gesendet werden,
     * an die {@link #eCommerceWarteschlange()} geleitet werden.
     *
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

    /**
     * Erstellt und konfiguriert ein {@link RabbitTemplate} für die Interaktion mit RabbitMQ.
     * Das Template wird so konfiguriert, dass es den {@link #jsonNachrichtenKonverter()}
     * für die Nachrichtenserialisierung und -deserialisierung verwendet.
     *
     * @param verbindungsFabrik Die {@link ConnectionFactory}, die für die Erstellung
     * der RabbitMQ-Verbindung verwendet wird.
     * @return Eine konfigurierte Instanz von {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitVorlage(final ConnectionFactory verbindungsFabrik) {
        final RabbitTemplate vorlage = new RabbitTemplate(verbindungsFabrik);
        vorlage.setMessageConverter(jsonNachrichtenKonverter());
        return vorlage;
    }
}