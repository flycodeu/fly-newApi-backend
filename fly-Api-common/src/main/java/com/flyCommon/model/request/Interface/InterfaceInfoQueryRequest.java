package com.flyCommon.model.request.Interface;

import com.flyCommon.model.constant.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode( callSuper = true )
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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


    private Integer port;

    private String IPAddress;


    /**
     * 调用次数
     */
    private Integer invokeCount;

//    /**
//     * 每百条的接口调用价格
//     */
//    private double price;


    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}