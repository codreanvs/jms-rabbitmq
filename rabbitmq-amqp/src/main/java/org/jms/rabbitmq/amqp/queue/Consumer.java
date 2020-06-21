package org.jms.rabbitmq.amqp.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;
import org.jms.rabbitmq.amqp.model.Wheel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Consumer {

    private final static String QUEUE_NAME = "winterQueue";

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("=== Waiting for messages. ===");

        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final byte[] byteArray = delivery.getBody();
            try {
                final Wheel wheel = (Wheel) deserialize(byteArray);

                System.out.println(String.format("> Received : '%s'.", wheel));
            } catch (final ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    public static Object deserialize(final byte[] byteArray) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        return objectInputStream.readObject();
    }

}
