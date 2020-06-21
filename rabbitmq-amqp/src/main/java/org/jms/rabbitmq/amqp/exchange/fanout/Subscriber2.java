package org.jms.rabbitmq.amqp.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;

import java.nio.charset.StandardCharsets;

public class Subscriber2 {

    private final static String EXCHANGE_NAME = "fanout-exchange";

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        final String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("=== Waiting for messages. ===");

        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(String.format("> Received '%s'.", message));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

}
