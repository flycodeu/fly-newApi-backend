package com.flyCommon.model.request.Order;

import lombok.Data;

import java.util.Date;

@Data
public class OrderRequest {


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 接口id
     */
    private Integer interfaceInfoId;

    /**
     * 总金额
     */
    private Double totalMoney;

//    /**
//     * 单价
//     */
//    private Double price;

    /**
     * 购买次数
     */
    private Integer buyCount;

    /**
     * 订单状态(0未支付，1已支付)
     */
    private Integer status;

    /**
     * 订单编号
     */
    private String orderSn;


    private String interfaceIfoName;

}
