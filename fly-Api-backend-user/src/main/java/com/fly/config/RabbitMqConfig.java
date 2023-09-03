package com.fly.config;

import com.fly.common.OrderConstant;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.exception.BusinessException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitMq配置
 */
public class RabbitMqConfig {
    public static void main(String[] args) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");

            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String channelExchange = OrderConstant.ORDER_EXCHANGE_NAME;
            channel.exchangeDeclare(channelExchange, "direct");

            String queueName = OrderConstant.ORDER_QUEUE_NAME;
            Map<String, Object> argus = new HashMap<String, Object>();
            // 10分钟过期
            argus.put("x-message-ttl", OrderConstant.ORDER_TTL);

            channel.queueDeclare(queueName, false, false, false, argus);

            channel.queueBind(queueName, channelExchange, OrderConstant.ORDER_ROUTING_KEY);
        } catch (Exception e) {
            // 自定义异常
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
