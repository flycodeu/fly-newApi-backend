package com.flyCommon.model.request.Interface;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求调用
 *
 * @TableName product
 */
@Data
public class InterfaceInfoUserInvokeRequest implements Serializable {

    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;


    private String token;


    /**
     * 接口地址url
     */
    private String url;


    /**
     * 端口号
     */
    private Integer port;

    /**
     * Ip地址
     */
    private String IPAddress;

    /**
     * 请求类型
     */
    private String method;


    private static final long serialVersionUID = 1L;
}