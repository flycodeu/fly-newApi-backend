package com.fly.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DirectConsumer {
    public static final String DIRECT_EXCHANGE = "exchange-direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");

        String queueNameAA = "direct-AA-queue";
        // 绑定路由"AA"
        channel.queueDeclare(queueNameAA, false, false, false, null);
        channel.queueBind(queueNameAA, DIRECT_EXCHANGE, "AA");


        String queueNameBB = "direct-BB-queue";
        // 绑定路由"BB"
        channel.queueDeclare(queueNameBB, false, false, false, null);
        channel.queueBind(queueNameBB, DIRECT_EXCHANGE, "BB");

        DeliverCallback deliverCallbackAA = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" AA Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        DeliverCallback deliverCallbackBB = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" BB Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        // aa,bb确认接收
        channel.basicConsume(queueNameBB, true, deliverCallbackBB, consumerTag -> {
        });

        channel.basicConsume(queueNameAA, true, deliverCallbackAA, consumerTag -> {
        });
    }
}
