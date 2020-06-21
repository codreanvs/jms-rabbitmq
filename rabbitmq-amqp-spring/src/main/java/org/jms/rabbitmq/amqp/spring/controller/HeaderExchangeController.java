package org.jms.rabbitmq.amqp.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/header/")
public class HeaderExchangeController {

    private final AmqpTemplate amqpTemplate;

    @PostMapping(value = "/{exchange}/{headerParam}")
    public String producer(@PathVariable("exchange") final String exchange,
                           @PathVariable("headerParam") final String headerParam,
                           @RequestBody final String wheel
    ) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("headerParam", headerParam);
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage(wheel, messageProperties);
        amqpTemplate.send(exchange, "", message);

        log.info("Sent (exchange = {}, headerParam = {}) '{}'.", exchange, headerParam, wheel);

        return "The message has been successfully sent to the RabbitMQ Header Exchange!";
    }

}
