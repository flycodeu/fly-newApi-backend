package com.flyCommon.model.request.User;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
