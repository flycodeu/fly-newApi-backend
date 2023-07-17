package com.fly.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.BaseResponse;
import com.fly.common.ResultUtils;
import com.fly.model.entity.InterfaceInfoNew;
import com.fly.model.request.DeleteRequest;
import com.fly.model.request.Interface.InterfaceInfoAddRequest;
import com.fly.model.request.Interface.InterfaceInfoQueryRequest;
import com.fly.model.request.Interface.InterfaceInfoUpdateRequest;
import com.fly.service.InterfaceInfoService;
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
     * @param token
     * @return
     */
    @PostMapping( "/add" )
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, String token) {
        Long aLong = interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest, token);
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
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping( "/interface/list" )
    public BaseResponse<List<InterfaceInfoNew>> getInterfaceInfoList(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        List<InterfaceInfoNew> allInterfaceInfoByList = interfaceInfoService.getAllInterfaceInfoByList(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByList);
    }


    /**
     * 接口分页
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping( "/interface/page" )
    public BaseResponse<Page<InterfaceInfoNew>> getInterfaceInfoPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfoNew> allInterfaceInfoByPage = interfaceInfoService.getAllInterfaceInfoByPage(interfaceInfoQueryRequest);
        return ResultUtils.success(allInterfaceInfoByPage);
    }
}
