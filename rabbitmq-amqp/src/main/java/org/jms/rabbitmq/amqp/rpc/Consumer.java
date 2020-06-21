package org.jms.rabbitmq.amqp.rpc;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;
import org.jms.rabbitmq.amqp.model.Season;
import org.jms.rabbitmq.amqp.model.Wheel;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Consumer {

    private final static String QUEUE_NAME = "rpcQueue";

    private static final List<Wheel> wheelList = Arrays.asList(
            Wheel.builder().id(1).manufacturer("Headway").model("HR601").season(Season.SUMMER).build(),
            Wheel.builder().id(2).manufacturer("Continental").model("SM1").season(Season.WINTER).build(),
            Wheel.builder().id(3).manufacturer("Bridgestone").model("Q2T").season(Season.SUMMER).build(),
            Wheel.builder().id(4).manufacturer("Michelin").model("Q2T").season(Season.WINTER).build()
    );

    private static Optional<Wheel> findWheelById(final int id) {
        return wheelList.stream()
                .filter(wheel -> wheel.getId() == id)
                .findFirst();
    }

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queuePurge(QUEUE_NAME);
        channel.basicQos(1);
        System.out.println("=== Waiting for messages. ===");

        Object monitor = new Object();
        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            Optional<Wheel> wheel = Optional.empty();
            try {
                final String wheelId = new String(delivery.getBody(), StandardCharsets.UTF_8);

                wheel = findWheelById(Integer.parseInt(wheelId));
            } catch (final RuntimeException exception) {
                exception.printStackTrace();
            } finally {
                final byte[] body = wheel.map(value -> value.toString().getBytes(StandardCharsets.UTF_8)).orElseGet(() -> "".getBytes(StandardCharsets.UTF_8));
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, body);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));
        while (true) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
