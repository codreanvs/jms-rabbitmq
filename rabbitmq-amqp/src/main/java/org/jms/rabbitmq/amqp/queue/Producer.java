package org.jms.rabbitmq.amqp.queue;

import com.rabbitmq.client.Channel;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;
import org.jms.rabbitmq.amqp.model.Season;
import org.jms.rabbitmq.amqp.model.Wheel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Producer {

    private final static String QUEUE_NAME = "winterQueue";

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        final Wheel wheel = Wheel.builder().id(1).manufacturer("Continental").model("SM1").season(Season.WINTER).build();
        final byte[] byteArray = getBytes(wheel);

        channel.basicPublish("", QUEUE_NAME, null, byteArray);

        System.out.println(String.format("> Sent : '%s'.", wheel));
    }

    public static byte[] getBytes(final Object object) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);

        return byteArrayOutputStream.toByteArray();
    }

}
