package com.fly.common;

/**
 * 订单rabbitMq常量
 */
public interface OrderConstant {

    String ORDER_QUEUE_NAME = "order_queue";
    String ORDER_EXCHANGE_NAME = "order_exchange";
    String ORDER_ROUTING_KEY = "order_routingKey";
    Long ORDER_TTL = 600000L;

    /**
     * 死信交换机，死性队列
     */
    String ORDER_DLX_DIRECT_EXCHANGE_NAME = "order_dlx_direct_exchange";
    String ORDER_DLX_DIRECT_QUEUE_NAME = "order_dlx_direct_queue";
    String ORDER_DLX_DIRECT_ROUTING_KEY_NAME = "order_dlx_direct_routingKey";
}
