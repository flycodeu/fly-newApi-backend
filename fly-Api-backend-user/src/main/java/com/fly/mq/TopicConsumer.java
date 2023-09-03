package com.fly.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class TopicConsumer {
    public static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 队列前端
        String queueName = "frontend_queue";
        channel.queueDeclare(queueName, true, false, false, null);
        // 绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "#.前端.#");

        // 队列后端
        String queueNameBB = "backend_queue";
        channel.queueDeclare(queueNameBB, true, false, false, null);
        // 绑定
        channel.queueBind(queueNameBB, EXCHANGE_NAME, "#.后端.#");

        // 队列产品需求
        String queueNameCC = "product_queue";
        channel.queueDeclare(queueNameCC, true, false, false, null);
        // 绑定
        channel.queueBind(queueNameCC, EXCHANGE_NAME, "#.产品.#");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" 前端 Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });


        DeliverCallback deliverCallbackBB = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [后端] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueNameBB, true, deliverCallbackBB, consumerTag -> {
        });


        DeliverCallback deliverCallbackCC = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" 产品 Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueNameCC, true, deliverCallbackCC, consumerTag -> {
        });
    }
}
