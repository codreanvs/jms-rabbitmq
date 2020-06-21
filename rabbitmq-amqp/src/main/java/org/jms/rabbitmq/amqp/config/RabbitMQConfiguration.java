package org.jms.rabbitmq.amqp.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConfiguration {

    public static ConnectionFactory getConnectionFactory() {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        return factory;
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        final Connection connection = getConnectionFactory().newConnection();

        return connection.createChannel();
    }

}
