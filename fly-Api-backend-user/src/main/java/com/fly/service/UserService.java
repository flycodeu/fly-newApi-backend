package com.fly.service;

import com.fly.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.model.request.User.UserAddRequest;
import com.fly.model.vo.LoginPhoneVo;
import com.fly.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author admin
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-07-13 18:24:51
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码

     * @return 脱敏后的用户信息
     */
    String userLogin(String userAccount, String userPassword);

    /**
     * 使用手机号登录
     *
     * @return
     */
    String userLoginByPhone(LoginPhoneVo loginPhoneVo);

    /**
     * 发送验证码
     *
     * @return
     */
    String sendCode(String phoneNum);

    /**
     * 获取登录用户
     *
     * @return
     */
    User getLoginUser();

    /**
     * 使用redis进行token
     *
     * @param token
     * @return
     */
    UserVO getLoginUserRedis(String token);

    /**
     * 退出
     * @return
     */
    boolean logout();

    /**
     * 添加用户，管理员权限
     * @param userAddRequest
     * @return
     */
    Long addUser(UserAddRequest userAddRequest);
}