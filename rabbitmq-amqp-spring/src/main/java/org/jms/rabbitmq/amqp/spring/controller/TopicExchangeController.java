package org.jms.rabbitmq.amqp.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/topic")
public class TopicExchangeController {

    private final AmqpTemplate amqpTemplate;

    @PostMapping(value = "/{exchange}/{route}")
    public String producer(@PathVariable("exchange") final String exchange,
                           @PathVariable("route") final String route,
                           @RequestBody final String wheel
    ) {
        amqpTemplate.convertAndSend(exchange, route, wheel);

        log.info("Sent (exchange = {}, headerParam = {}) '{}'.", exchange, route, wheel);

        return "The message has been successfully sent to the RabbitMQ Topic Exchange!";
    }

}
