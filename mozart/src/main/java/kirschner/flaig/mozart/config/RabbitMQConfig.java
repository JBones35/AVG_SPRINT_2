package kirschner.flaig.mozart.config;

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
 * Konfigurationsklasse für RabbitMQ-Beans.
 * Definiert Exchanges, Warteschlangen, Bindings zwischen ihnen,
 * einen Nachrichtenkonverter und ein RabbitTemplate.
 */
@Configuration
public class RabbitMQConfig {

    // --- CRM System Konfiguration ---
    /**
     * Der Name des Exchanges für Nachrichten aus dem CRM-System.
     */
    public static final String CRM_EXCHANGE_NAME = "crm.direct.exchange";
    /**
     * Der Name der Warteschlange für Aktualisierungen aus dem CRM-System.
     */
    public static final String CRM_WARTESCHLANGEN_NAME = "crm.updates.queue";
    /**
     * Der Routing Key, um CRM-Aktualisierungen an die entsprechende Warteschlange zu leiten.
     */
    public static final String CRM_ROUTING_KEY = "crm.updates.routingkey";


    // --- E-Commerce / ERP System Konfiguration ---
    /**
     * Der Name des Exchanges für Nachrichten aus dem E-Commerce-System (oft ERP-bezogen).
     */
    public static final String ECOMMERCE_EXCHANGE_NAME = "ecommerce.direct.exchange";
    /**
     * Der Name der Warteschlange für Statusaktualisierungen aus dem E-Commerce-System,
     * die typischerweise mit einem ERP-Austausch verbunden ist.
     */
    public static final String ECOMMERCE_STATUS_WARTESCHLANGE_NAME = "ecommerce.status.queue"; // Umbenannt für Klarheit
    /**
     * Der Routing Key, um E-Commerce-Statusaktualisierungen an die entsprechende Warteschlange zu leiten.
     */
    public static final String ECOMMERCE_STATUS_ROUTING_KEY = "ecommerce.status.routingkey";


    /**
     * Erstellt einen Nachrichtenkonverter, der Nachrichten in das JSON-Format serialisiert und deserialisiert.
     * Verwendet {@link Jackson2JsonMessageConverter}.
     *
     * @return Eine Instanz von {@link MessageConverter}.
     */
    @Bean
    public MessageConverter jsonNachrichtenKonverter() {
        return new Jackson2JsonMessageConverter();
    }

    // === CRM Konfiguration: Exchange, Queue und Binding ===

    /**
     * Definiert den Direct Exchange für das CRM-System.
     * Direct Exchanges leiten Nachrichten basierend auf einem exakten Übereinstimmen des Routing Keys weiter.
     * @return Eine Instanz von {@link DirectExchange} für das CRM-System.
     */
    @Bean
    public DirectExchange crmExchange() {
        return new DirectExchange(CRM_EXCHANGE_NAME);
    }

    /**
     * Definiert und erstellt die RabbitMQ-Warteschlange für das CRM-System.
     * Die Warteschlange ist nicht durebelfähig (non-durable).
     *
     * @return Eine Instanz von {@link Queue} für das CRM-System.
     */
    @Bean
    public Queue crmWarteschlange() {
        // durable: false, exclusive: false, autoDelete: false
        return new Queue(CRM_WARTESCHLANGEN_NAME, false, false, false);
    }

    /**
     * Erstellt ein Binding zwischen der CRM-Warteschlange und dem CRM-Exchange
     * unter Verwendung des definierten CRM-Routing-Keys.
     *
     * @param crmWarteschlange Die zu bindende CRM-Warteschlange.
     * @param crmExchange Der zu bindende CRM-Exchange.
     * @return Ein {@link Binding}-Objekt.
     */
    @Bean
    public Binding crmBinding(Queue crmWarteschlange, DirectExchange crmExchange) {
        return BindingBuilder.bind(crmWarteschlange).to(crmExchange).with(CRM_ROUTING_KEY);
    }


    // === E-Commerce / ERP Konfiguration: Exchange, Queue und Binding ===

    /**
     * Definiert den Direct Exchange für das E-Commerce/ERP-System.
     * @return Eine Instanz von {@link DirectExchange} für das E-Commerce/ERP-System.
     */
    @Bean
    public DirectExchange ecommerceExchange() {
        return new DirectExchange(ECOMMERCE_EXCHANGE_NAME);
    }

    /**
     * Definiert und erstellt die RabbitMQ-Warteschlange für das E-Commerce/ERP-System.
     * Die Warteschlange ist durebelfähig (persistent).
     *
     * @return Eine Instanz von {@link Queue} für das E-Commerce/ERP-System.
     */
    @Bean
    public Queue ecommerceStatusWarteschlange() { // Name der Methode angepasst
        // durable: true, exclusive: false, autoDelete: false
        return new Queue(ECOMMERCE_STATUS_WARTESCHLANGE_NAME, true, false, false);
    }

    /**
     * Erstellt ein Binding zwischen der E-Commerce-Status-Warteschlange und dem E-Commerce-Exchange
     * unter Verwendung des definierten E-Commerce-Status-Routing-Keys.
     *
     * @param ecommerceStatusWarteschlange Die zu bindende E-Commerce-Status-Warteschlange.
     * @param ecommerceExchange Der zu bindende E-Commerce-Exchange.
     * @return Ein {@link Binding}-Objekt.
     */
    @Bean
    public Binding ecommerceBinding(Queue ecommerceStatusWarteschlange, DirectExchange ecommerceExchange) {
        return BindingBuilder.bind(ecommerceStatusWarteschlange).to(ecommerceExchange).with(ECOMMERCE_STATUS_ROUTING_KEY);
    }


    /**
     * Erstellt und konfiguriert ein {@link RabbitTemplate}.
     * Das Template wird mit der bereitgestellten {@link ConnectionFactory} und dem
     * {@link #jsonNachrichtenKonverter()} konfiguriert.
     *
     * @param verbindungsFabrik Die RabbitMQ-Verbindungsfabrik.
     * @return Eine konfigurierte Instanz von {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitVorlage(final ConnectionFactory verbindungsFabrik) {
        final RabbitTemplate vorlage = new RabbitTemplate(verbindungsFabrik);
        vorlage.setMessageConverter(jsonNachrichtenKonverter());
        return vorlage;
    }
}