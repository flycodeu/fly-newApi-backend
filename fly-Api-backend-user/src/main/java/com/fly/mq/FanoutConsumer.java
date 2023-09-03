package com.fly.mq;

import com.rabbitmq.client.*;

public class FanoutConsumer {
    public static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();

        // 创建两个信道，将两个信道绑定到同一个交换机上
        Channel channelA = connection.createChannel();
        Channel channelB = connection.createChannel();

        Channel channelC = connection.createChannel();

        channelA.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueNameA = "A 队列";
        channelA.queueDeclare(queueNameA, false, false, false, null);
        channelA.queueBind(queueNameA, FANOUT_EXCHANGE_NAME, "");

        String queueNameB = "B 队列";
        channelA.queueDeclare(queueNameB, false, false, false, null);
        channelA.queueBind(queueNameB, FANOUT_EXCHANGE_NAME, "");


        String queueNameC = "C 队列";
        channelC.queueDeclare(queueNameC, false, false, false, null);


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallbackA = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" A Received '" + message + "'");
        };

        DeliverCallback deliverCallbackB = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" B Received '" + message + "'");
        };

        DeliverCallback deliverCallbackC = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" C Received '" + message + "'");
        };


        channelA.basicConsume(queueNameA, true, deliverCallbackA, consumerTag -> {
        });
        channelB.basicConsume(queueNameB, true, deliverCallbackB, consumerTag -> {
        });

        channelC.basicConsume(queueNameC, true, deliverCallbackC, consumerTag -> {
        });
    }
}
