package org.jms.rabbitmq.amqp.spring.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

//@Configuration
public class FanoutExchangeConfiguration {

    public static final String WINTER_QUEUE_NAME = "winterQueue";

    public static final String HEADWAY_QUEUE_NAME = "headwayQueue";

    public static final String Q2T_QUEUE_NAME = "q2tQueue";

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
    FanoutExchange exchange() {
        return new FanoutExchange("fanout-exchange");
    }

    @Bean
    Binding winterBinding(final Queue winterQueue, final FanoutExchange exchange) {
        return BindingBuilder
                .bind(winterQueue)
                .to(exchange);
    }

    @Bean
    Binding headwayBinding(final Queue headwayQueue, final FanoutExchange exchange) {
        return BindingBuilder
                .bind(headwayQueue)
                .to(exchange);
    }

    @Bean
    Binding q2tBinding(final Queue q2tQueue, final FanoutExchange exchange) {
        return BindingBuilder
                .bind(q2tQueue)
                .to(exchange);
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
