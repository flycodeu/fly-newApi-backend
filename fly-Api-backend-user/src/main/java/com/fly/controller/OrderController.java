package com.fly.controller;

import com.fly.annotation.AuthCheck;
import com.fly.common.BaseResponse;
import com.fly.common.ResultUtils;
import com.fly.constant.UserConstant;
import com.fly.service.OrderApiService;
import com.flyCommon.model.entity.OrderApi;
import com.flyCommon.model.request.Order.OrderRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@RequestMapping( "/order" )
@RestController
public class OrderController {
    @Resource
    private OrderApiService orderService;

    /**
     * 创建订单
     *
     * @param orderRequest
     * @return
     */
    @PostMapping( "/create" )
    public BaseResponse<Boolean> createOrder(@RequestBody OrderRequest orderRequest) {
        boolean b = orderService.addOrder(orderRequest);
        return ResultUtils.success(b);
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    @GetMapping( "/delete" )
    public BaseResponse<Boolean> deleteOrder(@RequestParam( "orderId" ) Long orderId) {
        boolean b = orderService.deleteOrderById(orderId);
        return ResultUtils.success(b);
    }

    /**
     * 根据订单id获取订单
     *
     * @param id
     * @return
     */
    @GetMapping( "/getOrderById" )
    public BaseResponse<OrderApi> getOrderById(@RequestParam( "id" ) Long id) {
        OrderApi orderById = orderService.getOrderById(id);
        return ResultUtils.success(orderById);
    }

    /**
     * 根据用户id获取所有的订单
     *
     * @param userId
     * @return
     */
    @GetMapping( "/getOrderByUserId" )
    public BaseResponse<List<OrderApi>> getOrderByUserId(@RequestParam( "userId" ) Long userId) {
        return ResultUtils.success(orderService.getListOrderByUserId(userId));
    }

    /**
     * 管理员获取所有的订单
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @GetMapping( "/getAllOrders" )
    public BaseResponse<List<OrderApi>> getAllOrders() {
        return ResultUtils.success(orderService.getListAllOrderAdmin());
    }


    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cacel")
    public BaseResponse<Boolean> cancelOrder(@RequestParam("orderId") Integer orderId) {
        boolean b = orderService.cancelOrder(orderId);
        return ResultUtils.success(b);
    }
}
