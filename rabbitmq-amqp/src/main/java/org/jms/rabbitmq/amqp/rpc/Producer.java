package org.jms.rabbitmq.amqp.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.jms.rabbitmq.amqp.config.RabbitMQConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Producer {

    private final static String QUEUE_NAME = "rpcQueue";

    public static void main(final String[] argv) throws Exception {
        final Channel channel = RabbitMQConfiguration.getChannel();
        final String correlationId = UUID.randomUUID().toString();
        final String wheelId = "1";
        final String replyQueueName = channel.queueDeclare().getQueue();
        final AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, wheelId.getBytes(StandardCharsets.UTF_8));
        System.out.println(String.format("=== Requested to get wheel with id = '%s'. ===", wheelId));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        final String basicConsumerTag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {});

        final String result = response.take();
        channel.basicCancel(basicConsumerTag);

        if (result.isEmpty()) {
            System.err.println(String.format("Wheel by id = '%s' not found.", wheelId));
        } else {
            System.out.println(String.format("> Received wheel (id = %s) : '%s'.", wheelId, result));
        }
    }

}
