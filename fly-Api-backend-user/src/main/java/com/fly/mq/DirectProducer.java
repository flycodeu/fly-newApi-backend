package com.fly.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class DirectProducer {
    public static final String DIRECT_EXCHANGE = "exchange-direct";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            // 输入两个数据，以空格分开，第一个指定路由键，第二个是输入的信息
            String userInput = scanner.nextLine();
            String[] strings = userInput.split(" ");
            if (strings.length < 1) {
                continue;
            }
            String routingKey = strings[0];
            String message = strings[1];
            channel.basicPublish(DIRECT_EXCHANGE, routingKey, null, message.getBytes());
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }
}
