package org.jms.rabbitmq.amqp.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Subscriber2 {

    private final static String QUEUE_NAME = "headwayAndQ2TQueue";

    private final static String EXCHANGE_NAME = "topic-exchange";

    private final static List<String> bindKeyList = Arrays.asList(
            "Headway.#",
            "*.Q2T.*"
    );

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        final String queueName = channel.queueDeclare(QUEUE_NAME, false, false, false, null).getQueue();

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
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
    }

}
