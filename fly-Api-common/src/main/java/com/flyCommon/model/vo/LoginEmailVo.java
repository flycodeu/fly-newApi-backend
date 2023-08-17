package com.flyCommon.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginEmailVo implements Serializable {

    private static final long serialVersionUID = -2315818919878704766L;
    /**
     * 手机号
     */
    private String email;

    /**
     * 验证码
     */
    private String code;


}
