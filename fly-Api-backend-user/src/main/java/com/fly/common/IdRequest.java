package com.fly.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求id
 *
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}