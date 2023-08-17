package com.flyCommon.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户ak，sk请求
 */
@Data
public class UserAKSKVo implements Serializable {
    private Long id;

    private String accessKey;

    private String secretKey;

    private String token;

    private static final long serialVersionUID = 6704438176641993890L;
}
