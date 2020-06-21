package org.jms.rabbitmq.amqp.spring.listener;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.jms.rabbitmq.amqp.spring.config.DirectExchangeConfiguration;
import org.jms.rabbitmq.amqp.spring.model.Wheel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class ContinentalAndQ2TQueueListener {

    @Transactional
    @RabbitListener(queues = {DirectExchangeConfiguration.CONTINENTAL_AND_Q2T_QUEUE_NAME})
    public void listen(final String input) {
        log.info("> Received '{}'", input);

        final Gson gson = new Gson();
        final Wheel wheel = gson.fromJson(input, Wheel.class);

        log.info("Received Wheel : '{}'.", wheel);
    }

}
