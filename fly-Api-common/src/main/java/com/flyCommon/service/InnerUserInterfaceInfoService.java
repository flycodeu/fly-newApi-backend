package com.flyCommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.vo.UserVO;

/**
 * @author admin
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2023-07-13 18:17:14
 */
public interface InnerUserInterfaceInfoService  {

    /**
     * 调用次数统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    Boolean invokeCount(Long interfaceInfoId, Long userId);

}
