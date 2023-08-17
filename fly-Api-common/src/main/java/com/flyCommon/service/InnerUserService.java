package com.flyCommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.vo.UserVO;

public interface InnerUserService {
    /**
     * 数据库查看是否分配给用户ak,sk
     *
     * @param accessKey
     * @return
     */
    User getInvokeUserAkSkValid(String accessKey);
}
