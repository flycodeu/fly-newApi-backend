package com.fly.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.IdRequest;
import com.flyCommon.model.request.Interface.*;
import com.flyCommon.model.request.UserInterface.UserInterfaceInfoCanAccess;

import java.util.List;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2023-07-13 18:26:14
 */
public interface InterfaceInfoService extends IService<InterfaceInfoNew> {

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest
     * @return
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest);

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @return
     */
    Boolean deleteInterfaceInfo(DeleteRequest deleteRequest);

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add           是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfoNew interfaceInfo, boolean add);

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     * 查看单个接口
     *
     * @param id
     * @return
     */
    InterfaceInfoNew getInterfaceInfoById(Long id);

    /**
     * 列表展示接口
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    List<InterfaceInfoNew> getAllInterfaceInfoByList(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 分页获取接口
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    Page<InterfaceInfoNew> getAllInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取用户创建的接口
     *
     * @param interfaceInfoUserQueryRequest
     * @return
     */
    Page<InterfaceInfoNew> getUserInterfaceInfoByPage(InterfaceInfoUserQueryRequest interfaceInfoUserQueryRequest);

    /**
     * 发布接口
     *
     * @param idRequest
     * @return
     */
    Boolean onLineInterfaceInfo(IdRequest idRequest);

    /**
     * 下线接口
     *
     * @param idRequest
     * @return
     */
    Boolean offLineInterfaceInfo(IdRequest idRequest);

    /**
     * 调用接口
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    Object invokeInterface(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest);

    /**
     * 获取线上的接口
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    Page<InterfaceInfoNew> getOnlineInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

}
