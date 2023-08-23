package com.fly.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.constant.UserConstant;
import com.fly.service.InterfaceInfoService;
import com.fly.annotation.AuthCheck;
import com.fly.common.BaseResponse;
import com.fly.common.ResultUtils;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.IdRequest;
import com.flyCommon.model.request.Interface.*;
import com.flyCommon.model.request.UserInterface.UserInterfaceInfoCanAccess;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping( "/interface" )
@RestController
public class InterfaceController {
    @Resource
    private InterfaceInfoService interfaceInfoService;


    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest
     * @return
     */
    @PostMapping( "/add" )
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        Long aLong = interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest);
        return ResultUtils.success(aLong);
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping( "/delete" )
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        Boolean aBoolean = interfaceInfoService.deleteInterfaceInfo(deleteRequest);
        return ResultUtils.success(aBoolean);
    }


    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping( "/update" )
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        Boolean aBoolean = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 获取接口
     *
     * @param id
     * @return
     */
    @GetMapping( "/get" )
    public BaseResponse<InterfaceInfoNew> getInterfaceInfoById(Long id) {
        InterfaceInfoNew interfaceInfoById = interfaceInfoService.getInterfaceInfoById(id);
        return ResultUtils.success(interfaceInfoById);
    }


    /**
     * 接口列表
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping( "/interface/list" )
    public BaseResponse<List<InterfaceInfoNew>> getInterfaceInfoList(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        List<InterfaceInfoNew> allInterfaceInfoByList = interfaceInfoService.getAllInterfaceInfoByList(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByList);
    }


    /**
     * 接口分页
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping( "/interface/page" )
    public BaseResponse<Page<InterfaceInfoNew>> getInterfaceInfoPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfoNew> allInterfaceInfoByPage = interfaceInfoService.getAllInterfaceInfoByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByPage);
    }


    /**
     * 接口分页 上线
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping( "/interface/onlinePage" )
    public BaseResponse<Page<InterfaceInfoNew>> getOnlineInterfaceInfoPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfoNew> allInterfaceInfoByPage = interfaceInfoService.getOnlineInterfaceInfoByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByPage);
    }

    /**
     * 管理员上线接口
     *
     * @param idRequest
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @PostMapping( "/onLine" )
    public BaseResponse<Boolean> onLineInterfaceInfo(@RequestBody IdRequest idRequest) {
        Boolean aBoolean = interfaceInfoService.onLineInterfaceInfo(idRequest);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 管理员下线接口
     *
     * @param idRequest
     * @return
     */
    @AuthCheck( mustRole = UserConstant.ADMIN_ROLE )
    @PostMapping( "/downLine" )
    public BaseResponse<Boolean> offLineInterfaceInfo(@RequestBody IdRequest idRequest) {
        Boolean aBoolean = interfaceInfoService.offLineInterfaceInfo(idRequest);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 调用接口
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping( "/invoke" )
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        Object o = interfaceInfoService.invokeInterface(interfaceInfoInvokeRequest);
        return ResultUtils.success(o);
    }

    /**
     * 返回当前用户创建的接口
     * @param userQueryRequest
     * @return
     */
    @PostMapping( "/userInterface" )
    public BaseResponse<Page<InterfaceInfoNew>> getUserInterfaceInfoByPage(@RequestBody InterfaceInfoUserQueryRequest userQueryRequest) {
        Page<InterfaceInfoNew> userInterfaceInfoByPage = interfaceInfoService.getUserInterfaceInfoByPage(userQueryRequest);
        return ResultUtils.success(userInterfaceInfoByPage);
    }
}
