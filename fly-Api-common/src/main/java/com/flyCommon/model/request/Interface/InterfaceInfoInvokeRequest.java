package com.flyCommon.model.request.Interface;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求调用
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private Long id;

    /**
     * 请求参数
     */
    private String userRequestParams;


    private String token;


    private static final long serialVersionUID = 1L;
}