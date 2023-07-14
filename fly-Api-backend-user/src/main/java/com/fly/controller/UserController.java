package com.fly.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.annotation.AuthCheck;
import com.fly.common.BaseResponse;
import com.fly.common.ErrorCode;
import com.fly.common.ResultUtils;
import com.fly.constant.UserConstant;
import com.fly.exception.BusinessException;
import com.fly.model.entity.User;
import com.fly.model.request.User.UserAddRequest;
import com.fly.model.request.User.UserLoginRequest;
import com.fly.model.request.User.UserRegisterRequest;
import com.fly.model.vo.LoginPhoneVo;
import com.fly.model.vo.LoginUserVO;
import com.fly.model.vo.UserVO;
import com.fly.service.UserService;
import com.fly.utils.RedisConstants;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import java.util.Map;

@RequestMapping( "/user" )
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 账号登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping( "/account/login" )
    public BaseResponse<String> loginAccount(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
       String token = userService.userLogin(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword());
        return ResultUtils.success(token);
    }

    /**
     * 账号注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping( "/register" )
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        long userId = userService.userRegister(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(), userRegisterRequest.getCheckPassword());
        return ResultUtils.success(userId);
    }

    /**
     * 发送验证码
     *
     * @param phoneNum
     * @return
     */
    @GetMapping( "/code" )
    public BaseResponse<String> sendCode(@Param( "phoneNum" ) String phoneNum) {
        return ResultUtils.success(userService.sendCode(phoneNum));
    }

    /**
     * 手机号发送
     *
     * @param loginPhoneVo
     * @return
     */
    @PostMapping( "/phone/login" )
    public BaseResponse<String> loginPhone(@RequestBody LoginPhoneVo loginPhoneVo) {
        return ResultUtils.success(userService.userLoginByPhone(loginPhoneVo));
    }

    /**
     * 获取token
     *
     * @param token
     * @return
     */
    @GetMapping( "/get/login/token" )
    public BaseResponse<UserVO> getLoginUserToken(String token) {
        UserVO userVO = userService.getLoginUserRedis(token);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取登录用户
     *

     * @return
     */
    @GetMapping( "/get/login" )
    public BaseResponse<UserVO> getLoginUser() {
        User loginUser = userService.getLoginUser();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(loginUser, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @DeleteMapping( "/logout" )
    public BaseResponse<Boolean> logout() {
        boolean logout = userService.logout();
        return ResultUtils.success(logout);
    }


    @PostMapping( "/add" )
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(userService.addUser(userAddRequest));
    }



}
