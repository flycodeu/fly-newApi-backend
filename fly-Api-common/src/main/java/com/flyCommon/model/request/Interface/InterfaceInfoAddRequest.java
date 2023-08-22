package com.flyCommon.model.request.Interface;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {


    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * ip地址
     */
    private String IPAddress;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 调用次数
     */
    private Integer invokeCount;

//    /**
//     * 每百条的接口调用价格
//     */
//    private double price;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;


    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;


    private String token;


    /**
     * 接口方法名字
     */
    private String methodName;

    /**
     * sdk对应的路径
     */
    private String sdkPath;


    private static final long serialVersionUID = 1L;
}