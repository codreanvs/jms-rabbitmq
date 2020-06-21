package org.jms.rabbitmq.amqp.exchange.topic;

import com.rabbitmq.client.Channel;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;
import org.jms.rabbitmq.amqp.model.Season;
import org.jms.rabbitmq.amqp.model.Wheel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Publisher {

    private final static String EXCHANGE_NAME = "topic-exchange";

    private static Map<String, Wheel> wheelMap() {
        Map<String, Wheel> wheelMap = new HashMap<>();
        wheelMap.put("Headway.HR601.summer",    Wheel.builder().id(1).manufacturer("Headway").model("HR601").season(Season.WINTER).build());
        wheelMap.put("Continental.SM1.winter",  Wheel.builder().id(2).manufacturer("Continental").model("SM1").season(Season.WINTER).build());
        wheelMap.put("Bridgestone.Q2T.summer",  Wheel.builder().id(3).manufacturer("Bridgestone").model("Q2T").season(Season.SUMMER).build());
        wheelMap.put("Michelin.Q2T.winter",     Wheel.builder().id(4).manufacturer("Michelin").model("Q2T").season(Season.WINTER).build());

        return wheelMap;
    }

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        wheelMap().forEach((key, value) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, key, null, value.toString().getBytes(StandardCharsets.UTF_8));

                System.out.println(String.format(
                        "> Sent '%s' to exchange '%s' and routing key '%s'.",
                        value, EXCHANGE_NAME, key
                ));
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        });
    }

}
