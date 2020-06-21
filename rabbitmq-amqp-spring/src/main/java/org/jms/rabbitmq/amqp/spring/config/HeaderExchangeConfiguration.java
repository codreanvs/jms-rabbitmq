package org.jms.rabbitmq.amqp.spring.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

//@Configuration
public class HeaderExchangeConfiguration {

    public static final String WINTER_QUEUE_NAME = "winterQueue";

    public static final String HEADWAY_QUEUE_NAME = "winterQueue";

    public static final String Q2T_QUEUE_NAME = "winterQueue";

    @Bean
    Queue winterQueue() {
        return new Queue(WINTER_QUEUE_NAME, false);
    }

    @Bean
    Queue headwayQueue() {
        return new Queue(HEADWAY_QUEUE_NAME, false);
    }

    @Bean
    Queue q2tQueue() {
        return new Queue(Q2T_QUEUE_NAME, false);
    }

    @Bean
    HeadersExchange headerExchange() {
        return new HeadersExchange("header-exchange");
    }

    @Bean
    Binding winterBinding(final Queue winterQueue, final HeadersExchange headerExchange) {
        return BindingBuilder
                .bind(winterQueue)
                .to(headerExchange)
                .where("headerParam")
                .matches("winter");
    }

    @Bean
    Binding headwayBinding(final Queue headwayQueue, final HeadersExchange headerExchange) {
        return BindingBuilder
                .bind(headwayQueue)
                .to(headerExchange)
                .where("headerParam")
                .matches("finance");
    }

    @Bean
    Binding q2tBinding(final Queue q2tQueue, final HeadersExchange headerExchange) {
        return BindingBuilder
                .bind(q2tQueue)
                .to(headerExchange)
                .where("headerParam")
                .matches("Q2T");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageListenerContainer messageListenerContainer(final ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        return simpleMessageListenerContainer;
    }

    public AmqpTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

}
