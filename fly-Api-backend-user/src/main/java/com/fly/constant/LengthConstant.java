package com.fly.constant;

import lombok.Data;

/**
 * 主要是一些长度的确定
 */
public interface LengthConstant {
    /**
     * 用户账号长度
     */
    Integer USERACCOUNTLENGTH = 4;

    /**
     * 用户密码长度
     */
    Integer USERPASSEORDLENGTH=6;

    /**
     * 用户检验密码长度
     */
    Integer CHECKPASSEORDLENGTH=6;
}
