package org.jms.rabbitmq.amqp.spring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DirectExchangeConfiguration {

    public static final String WINTER_QUEUE_NAME = "winterQueue";

    public static final String CONTINENTAL_AND_Q2T_QUEUE_NAME = "continentalAndQ2TQueue";

    @Bean
    Queue winterQueue() {
        return new Queue(WINTER_QUEUE_NAME, false);
    }

    @Bean
    Queue continentalAndQ2TQueue() {
        return new Queue(CONTINENTAL_AND_Q2T_QUEUE_NAME, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Binding winterBinding(final Queue winterQueue, final DirectExchange exchange) {
        return BindingBuilder
                .bind(winterQueue)
                .to(exchange)
                .with("winter");
    }

    @Bean
    Binding continentalAndQ2TBinding(final Queue continentalAndQ2TQueue, final DirectExchange exchange) {
        return BindingBuilder
                .bind(continentalAndQ2TQueue)
                .to(exchange)
                .with("Continental");
    }

    @Bean
    Binding q2tBinding(final Queue continentalAndQ2TQueue, final DirectExchange exchange) {
        return BindingBuilder
                .bind(continentalAndQ2TQueue)
                .to(exchange)
                .with("Q2T");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageListenerContainer messageListenerContainer(
            final PlatformTransactionManager transactionManager,
            final ConnectionFactory connectionFactory
    ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setTransactionManager(transactionManager);
        simpleMessageListenerContainer.setChannelTransacted(true);

        return simpleMessageListenerContainer;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(final ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

//    public AmqpTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//
//        return rabbitTemplate;
//    }

}
