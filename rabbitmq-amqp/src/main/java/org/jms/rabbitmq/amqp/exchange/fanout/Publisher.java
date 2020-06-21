package org.jms.rabbitmq.amqp.exchange.fanout;

import com.rabbitmq.client.Channel;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;
import org.jms.rabbitmq.amqp.model.Season;
import org.jms.rabbitmq.amqp.model.Wheel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Publisher {

    private final static String EXCHANGE_NAME = "fanout-exchange";

    private static final List<Wheel> wheelList = Arrays.asList(
            Wheel.builder().id(1).manufacturer("Headway").model("HR601").season(Season.SUMMER).build(),
            Wheel.builder().id(2).manufacturer("Continental").model("SM1").season(Season.WINTER).build(),
            Wheel.builder().id(3).manufacturer("Bridgestone").model("Q2T").season(Season.SUMMER).build(),
            Wheel.builder().id(4).manufacturer("Michelin").model("Q2T").season(Season.WINTER).build()
    );

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        wheelList.forEach(wheel -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, "", null, wheel.toString().getBytes(StandardCharsets.UTF_8));

                System.out.println(String.format("> Sent '%s' to exchange '%s'.", wheel, EXCHANGE_NAME));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        });
    }

}
