package com.fly.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.constant.UserConstant;
import com.fly.exception.BusinessException;
import com.fly.service.UserService;
import com.flyCommon.model.request.User.*;
import com.fly.annotation.AuthCheck;
import com.fly.common.BaseResponse;
import com.fly.common.ErrorCode;
import com.fly.common.ResultUtils;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.vo.LoginPhoneVo;
import com.flyCommon.model.vo.UserAKSKVo;
import com.flyCommon.model.vo.UserVO;
import com.fly.utils.ThrowUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping( "/user" )
@RestController
public class UserController {
    @Resource
    private UserService userService;


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
    public BaseResponse<Boolean> logout(String token) {
        boolean logout = userService.logout(token);
        return ResultUtils.success(logout);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping( "/add" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(userService.addUser(userAddRequest));
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping( "/updateUser" )
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResultUtils.success(userService.updateUser(userUpdateRequest));
    }


    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping( "/deleteUser" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.success(userService.deleteUser(deleteRequest));
    }

    /**
     * 分页展示数据(管理员)
     * 这个方法模糊查询不起作用
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping( "/list/page/user" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Page<UserVO>> listPageUser(@RequestBody UserQueryRequest userQueryRequest) {
        return ResultUtils.success(userService.listPageUsers(userQueryRequest));
    }


    /**
     * 分页查询数据，支持模糊查询
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping( "/list/page/vo" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 获取AK，sk
     * @param userAKSKVo
     * @return
     */
    @PostMapping( "/getClient" )
    public BaseResponse<UserAKSKVo> getAkSkUser(@RequestBody UserAKSKVo userAKSKVo) {
        UserAKSKVo userAkSkByToken = userService.getUserAkSkByToken(userAKSKVo);
        return ResultUtils.success(userAkSkByToken);
    }

    /**
     * 更新Ak，sk
     * @param userAKSKVo
     * @return
     */
    @PostMapping( "/updateClient" )
    public BaseResponse<UserAKSKVo> updateAkSk(@RequestBody UserAKSKVo userAKSKVo) {
        UserAKSKVo userAKSKVo1 = userService.updateUserAkSk(userAKSKVo);
        return ResultUtils.success(userAKSKVo1);
    }
}
