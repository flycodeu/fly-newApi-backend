package com.flyCommon.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改接口上线下线id
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}