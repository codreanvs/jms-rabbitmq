package org.jms.rabbitmq.amqp.spring.listener;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.jms.rabbitmq.amqp.spring.config.FanoutExchangeConfiguration;
import org.jms.rabbitmq.amqp.spring.model.Wheel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class HeadwayQueueListener {

    @RabbitListener(queues = {FanoutExchangeConfiguration.HEADWAY_QUEUE_NAME})
    public void listen(final String input) {
        log.info("> Received '{}'", input);

        final Gson gson = new Gson();
        final Wheel wheel = gson.fromJson(input, Wheel.class);

        log.info("Received Wheel : '{}'.", wheel);
    }

}
