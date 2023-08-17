package com.fly.service.impl.Inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.ErrorCode;
import com.fly.exception.BusinessException;
import com.fly.service.InterfaceInfoService;
import com.fly.service.UserInterfaceInfoService;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.UserInterface.UserInterfaceInfoCanAccess;
import com.flyCommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 内部用户接口
 */
@Component
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    /**
     * 调用接口次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public Boolean invokeCount(Long interfaceInfoId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }



}
