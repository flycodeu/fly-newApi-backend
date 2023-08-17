package com.fly.utils;


import com.flyCommon.model.vo.UserVO;

public class UserHolder {
    private static final ThreadLocal<UserVO> tl = new ThreadLocal<>();

    public static void saveUser(UserVO userVO){
        tl.set(userVO);
    }

    public static UserVO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
