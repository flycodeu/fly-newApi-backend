package com.fly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.OrderApi;
import com.flyCommon.model.request.Order.OrderRequest;

import java.math.BigInteger;
import java.util.List;


/**
 * @author admin
 * @description 针对表【order_api(订单)】的数据库操作Service
 * @createDate 2023-08-25 17:43:00
 */
public interface OrderApiService extends IService<OrderApi> {

    /**
     * 添加订单
     *
     * @param orderRequest
     * @return
     */
    boolean addOrder(OrderRequest orderRequest);

    /**
     * 查询订单
     *
     * @param id
     * @return
     */
    OrderApi getOrderById(Long id);

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    boolean deleteOrderById(Long id);

    /**
     * 展示用户的订单
     *
     * @param userId
     * @return
     */
    List<OrderApi> getListOrderByUserId(Long userId);


    /**
     * 管理员展示所有的订单
     *
     * @return
     */
    List<OrderApi> getListAllOrderAdmin();

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    boolean cancelOrder(Integer orderId);
}
