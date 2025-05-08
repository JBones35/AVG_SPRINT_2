package kirschner.flaig.beethoven.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Ein Log4j Appender, der Log-Nachrichten an einen RabbitMQ Exchange sendet.
 * Dieser Appender verbindet sich mit einem RabbitMQ-Server, deklariert einen Exchange sowie eine Queue
 * und leitet formatierte Log-Ereignisse an diesen Exchange weiter.
 */
@Plugin(name = "RabbitMQ", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class RabbitMQAppender extends AbstractAppender {

    /**
     * Die RabbitMQ-Verbindung.
     */
    private transient Connection verbindung;
    /**
     * Der RabbitMQ-Kanal.
     */
    private transient Channel kanal;
    /**
     * Der Name des RabbitMQ-Exchanges, an den die Nachrichten gesendet werden.
     */
    private final String austauschName;

    /**
     * Geschützter Konstruktor für den RabbitMQAppender.
     * Initialisiert den Appender und baut die Verbindung zu RabbitMQ auf.
     *
     * @param name Der Name des Appenders.
     * @param filter Der Filter, der auf Ereignisse angewendet wird, bevor sie protokolliert werden.
     * @param layout Das Layout, das zum Formatieren des Log-Ereignisses verwendet wird.
     * @param ignoriereAusnahmen Gibt an, ob Ausnahmen, die während des Anhängens auftreten, ignoriert werden sollen.
     * @param warteschlangenName Der Name der zu deklarierenden RabbitMQ-Queue.
     * @param austauschName Der Name des zu deklarierenden RabbitMQ-Exchanges.
     * @param host Der Hostname des RabbitMQ-Servers.
     * @param port Der Port des RabbitMQ-Servers.
     * @param benutzername Der Benutzername für die RabbitMQ-Verbindung.
     * @param passwort Das Passwort für die RabbitMQ-Verbindung.
     */
    protected RabbitMQAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
                               final boolean ignoriereAusnahmen, final String warteschlangenName, final String austauschName, final String host,
                               final int port, final String benutzername, final String passwort) {
        super(name, filter, layout, ignoriereAusnahmen, null);
        this.austauschName = austauschName;

        try {
            ConnectionFactory verbindungsFabrik = new ConnectionFactory();
            verbindungsFabrik.setHost(host);
            verbindungsFabrik.setPort(port);
            verbindungsFabrik.setUsername(benutzername);
            verbindungsFabrik.setPassword(passwort);
            this.verbindung = verbindungsFabrik.newConnection();
            this.kanal = this.verbindung.createChannel();

            this.kanal.exchangeDeclare(this.austauschName, "fanout", true);
            this.kanal.queueDeclare(warteschlangenName, false, false, false, null);
            this.kanal.queueBind(warteschlangenName, this.austauschName, "");

        } catch (IOException | TimeoutException e) {
            LOGGER.error("Failed to connect to RabbitMQ", e);
        }
    }

    /**
     * Factory-Methode zum Erstellen und Konfigurieren eines RabbitMQAppenders.
     * Diese Methode wird von Log4j verwendet, um den Appender basierend auf der Konfiguration zu instanziieren.
     *
     * @param name Der Name des Appenders.
     * @param warteschlangenName Der Name der Queue.
     * @param austauschName Der Name des Exchanges.
     * @param host Der Hostname des RabbitMQ-Servers.
     * @param port Der Port des RabbitMQ-Servers.
     * @param benutzername Der Benutzername für die RabbitMQ-Verbindung.
     * @param passwort Das Passwort für die RabbitMQ-Verbindung.
     * @param layout Das Layout, das zum Formatieren von Log-Ereignissen verwendet wird. Wenn null, wird ein Standard-PatternLayout verwendet.
     * @param filter Der Filter, der auf Ereignisse angewendet wird.
     * @return Eine Instanz des konfigurierten RabbitMQAppenders.
     */
    @PluginFactory
    public static RabbitMQAppender createAppender(
            @PluginAttribute("name") final String name,
            @PluginAttribute("queueName") final String warteschlangenName,
            @PluginAttribute("exchangeName") final String austauschName,
            @PluginAttribute("host") final String host,
            @PluginAttribute("port") final int port,
            @PluginAttribute("username") final String benutzername,
            @PluginAttribute("password") final String passwort,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter) {

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        // Der Parameter ignoreExceptions wird im Konstruktoraufruf standardmäßig auf true gesetzt.
        return new RabbitMQAppender(name, filter, layout, true, warteschlangenName, austauschName, host, port, benutzername, passwort);
    }

    /**
     * Hängt ein Log-Ereignis an. Das formatierte Ereignis wird als Nachricht an den konfigurierten RabbitMQ Exchange gesendet.
     * Stellt sicher, dass der Kanal für die Veröffentlichung verfügbar ist.
     *
     * @param logEreignis Das zu protokollierende Log-Ereignis.
     */
    @Override
    public void append(final LogEvent logEreignis) {
        if (this.kanal == null || !this.kanal.isOpen()) {
            LOGGER.error("Cannot log to RabbitMQ, channel is not available or closed for appender {}.", this.getName());
            return;
        }
        try {
            final byte[] nachricht = getLayout().toByteArray(logEreignis);
            this.kanal.basicPublish(this.austauschName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, nachricht);
        } catch (IOException e) {
            LOGGER.error("Error while sending log to RabbitMQ for appender {}: {}", this.getName(), e.getMessage(), e);
        }
    }

    /**
     * Stoppt den Appender und gibt die verwendeten Ressourcen frei.
     * Schließt den RabbitMQ-Kanal und die Verbindung.
     */
    @Override
    public void stop() {
        super.stop();
        try {
            if (this.kanal != null && this.kanal.isOpen()) {
                this.kanal.close();
            }
            if (this.verbindung != null && this.verbindung.isOpen()) {
                this.verbindung.close();
            }
        } catch (TimeoutException | IOException ignoriert) {
            // Fehler beim Schließen der Ressourcen werden protokolliert, aber ignoriert, um den Stopp-Prozess nicht zu blockieren.
            LOGGER.warn("Exception ignored while closing RabbitMQ resources for appender {}: {}", this.getName(), ignoriert.getMessage());
        }
    }
}