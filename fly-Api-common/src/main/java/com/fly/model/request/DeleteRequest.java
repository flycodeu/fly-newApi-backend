package com.fly.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}