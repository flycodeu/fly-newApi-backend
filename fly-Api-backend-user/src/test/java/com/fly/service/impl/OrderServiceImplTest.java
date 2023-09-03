package com.fly.service.impl;

import com.flyCommon.model.entity.OrderApi;
import com.flyCommon.model.request.Order.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {
    @Resource
    private OrderApiServiceImpl orderService;

    @Test
    void addOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(6);
        orderRequest.setInterfaceInfoId(167);
        orderRequest.setTotalMoney(1.0);
        orderRequest.setBuyCount(100);
        orderRequest.setStatus(0);
//        boolean b = orderService.addOrder(orderRequest);
//        System.out.println(b);
    }

    @Test
    void getOrderById() {
        OrderApi orderById = orderService.getOrderById(1695010913939161090L);
        System.out.println(orderById);
    }

    @Test
    void deleteOrderById() {
    }

    @Test
    void getListOrderByUserId() {
        List<OrderApi> listOrderByUserId =
                orderService.getListOrderByUserId(6L);

        listOrderByUserId.forEach(System.out::println);
    }

    @Test
    void getListAllOrderAdmin() {
    }


    @Test
    void name() {
//        boolean b = orderService.cancelOrder(1695010913939161090L);
//        System.out.println(b);
    }
}