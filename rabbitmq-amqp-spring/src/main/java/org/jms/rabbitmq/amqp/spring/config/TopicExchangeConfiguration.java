package org.jms.rabbitmq.amqp.spring.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

//@Configuration
public class TopicExchangeConfiguration {

    public static final String WINTER_QUEUE_NAME = "winterQueue";

    public static final String HEADWAY_AND_Q2T_QUEUE_NAME = "winterQueue";

    public static final String ALL_QUEUE_NAME = "allQueue";

    @Bean
    Queue winterQueue() {
        return new Queue(WINTER_QUEUE_NAME, false);
    }

    @Bean
    Queue headwayAndQ2TQueue() {
        return new Queue(HEADWAY_AND_Q2T_QUEUE_NAME, false);
    }

    @Bean
    Queue allQueue() {
        return new Queue(ALL_QUEUE_NAME, false);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topic-exchange");
    }

    @Bean
    Binding winterBinding(final Queue winterQueue, final TopicExchange topicExchange) {
        return BindingBuilder
                .bind(winterQueue)
                .to(topicExchange)
                .with("*.*.winter");
    }

    @Bean
    Binding headwayBinding(final Queue headwayAndQ2TQueue, final TopicExchange topicExchange) {
        return BindingBuilder
                .bind(headwayAndQ2TQueue)
                .to(topicExchange)
                .with("Headway.#");
    }

    @Bean
    Binding q2tBinding(final Queue headwayAndQ2TQueue, final TopicExchange topicExchange) {
        return BindingBuilder
                .bind(headwayAndQ2TQueue)
                .to(topicExchange)
                .with("*.Q2T.*");
    }

    @Bean
    Binding allBinding(final Queue allQueue, final TopicExchange topicExchange) {
        return BindingBuilder
                .bind(allQueue)
                .to(topicExchange)
                .with("#");
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

        rabbitTemplate.setChannelTransacted(true);

        return rabbitTemplate;
    }
}
