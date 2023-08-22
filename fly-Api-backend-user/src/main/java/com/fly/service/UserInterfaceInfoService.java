package com.fly.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.DeleteRequest;
import com.flyCommon.model.request.UserInterface.*;
import com.flyCommon.model.vo.UserInterfaceInfoVo;

import java.util.List;

/**
* @author admin
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-07-13 18:17:14
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 添加用户接口
     *
     * @return
     */
    Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest);

    /**
     * 删除用户接口
     *
     * @param deleteRequest
     * @return
     */
    Boolean deleteInterfaceInfo(DeleteRequest deleteRequest);


    /**
     * 更新用户接口
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    Boolean updateInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest);

    /**
     * 查看单个用户接口
     *
     * @param id
     * @return
     */
    UserInterfaceInfo getUserInterfaceInfoById(Long id);

    /**
     * 列表展示用户接口
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    List<UserInterfaceInfo> getAllInterfaceInfoByList(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 分页获取用户接口
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    Page<UserInterfaceInfo> getAllInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);


    Page<UserInterfaceInfoVo> getAllInterfaceInfoDetailByPage(UserInterfaceInfoVoRequest userInterfaceInfoQueryRequest);

    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add           是否为创建校验
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);


    /**
     * 调用次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    Boolean invokeCount(Long interfaceInfoId, Long userId);

    /**
     * 剩余次数
     * @param userInterfaceInfoCount
     * @return
     */
    Integer getUserInterfaceInfoInvokeCount(UserInterfaceInfoCount userInterfaceInfoCount);

    /**
     * 是否可以添加用户接口表
     * @param userId
     * @return
     */
    Boolean addUserInterfaceInfoInToTable(Long userId);
}
