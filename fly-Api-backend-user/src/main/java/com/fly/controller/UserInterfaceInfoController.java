package com.fly.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.annotation.AuthCheck;
import com.fly.common.BaseResponse;
import com.fly.common.ResultUtils;
import com.fly.constant.UserConstant;
import com.fly.service.UserInterfaceInfoService;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.UserInterface.*;
import com.flyCommon.model.vo.UserInterfaceInfoVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping( "/userInterfaceInfo" )
@RestController
public class UserInterfaceInfoController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    /**
     * 添加新的用户接口表数据
     *
     * @param userInterfaceInfoAddRequest
     * @return
     */
    @PostMapping( "/add" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        Long aLong = userInterfaceInfoService.addUserInterfaceInfo(userInterfaceInfoAddRequest);
        return ResultUtils.success(aLong);
    }

    /**
     * 修改用户接口表
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @PostMapping( "/update" )
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        Boolean aBoolean = userInterfaceInfoService.updateInterfaceInfo(userInterfaceInfoUpdateRequest);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 删除用户接口表
     *
     * @param deleteRequest
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @PostMapping( "/delete" )
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        Boolean aBoolean = userInterfaceInfoService.deleteInterfaceInfo(deleteRequest);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 获取单个用户接口信息
     *
     * @param id
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @GetMapping( "/get" )
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(Long id) {
        UserInterfaceInfo byId = userInterfaceInfoService.getById(id);
        return ResultUtils.success(byId);
    }

    /**
     * 列表展示所有的用户接口信息
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping( "/getAll/List" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<List<UserInterfaceInfo>> getAllUserInterfaceInfoByList(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        List<UserInterfaceInfo> allInterfaceInfoByList = userInterfaceInfoService.getAllInterfaceInfoByList(userInterfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByList);
    }

    /**
     * 分页展示所有的用户接口信息
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping( "/getAll/page" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Page<UserInterfaceInfo>> getAllUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfo> allInterfaceInfoByPage = userInterfaceInfoService.getAllInterfaceInfoByPage(userInterfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByPage);
    }


    /**
     * 返回详细的接口信息
     * @param userInterfaceInfoVoRequest
     * @return
     */
    @PostMapping( "/getAll/datail/page" )
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    public BaseResponse<Page<UserInterfaceInfoVo>> getAllUserInterfaceInfoDetailByPage(@RequestBody UserInterfaceInfoVoRequest userInterfaceInfoVoRequest) {
        Page<UserInterfaceInfoVo> allInterfaceInfoByPage = userInterfaceInfoService.getAllInterfaceInfoDetailByPage(userInterfaceInfoVoRequest);
        return ResultUtils.success(allInterfaceInfoByPage);
    }

    /**
     * 获取调用次数
     * @param userInterfaceInfoCount
     * @return
     */
    @PostMapping( "/get/interface" )
    public BaseResponse<Integer> invokeCount(@RequestBody UserInterfaceInfoCount userInterfaceInfoCount) {
        Integer leftCount = userInterfaceInfoService.getUserInterfaceInfoInvokeCount(userInterfaceInfoCount);
        return ResultUtils.success(leftCount);
    }


    /**
     * 添加接口调用次数
     * @param userId
     * @return
     */
    @GetMapping( "/add/table" )
    public BaseResponse<Boolean> addUserInterfaceInToTable(Long userId) {
        return ResultUtils.success(userInterfaceInfoService.addUserInterfaceInfoInToTable(userId));
    }


}
