package org.jms.rabbitmq.amqp.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Subscriber2 {

    private final static String EXCHANGE_NAME = "direct-exchange";

    private final static List<String> bindKeyList = Arrays.asList(
            "Continental",
            "Q2T"
    );

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        final String queueName = channel.queueDeclare().getQueue();

        bindKeyList.forEach(bindKey -> {
            try {
                channel.queueBind(queueName, EXCHANGE_NAME, bindKey);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        });
        System.out.println("=== Waiting for messages. ===");

        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(String.format("> Received '%s'.", message));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

}
