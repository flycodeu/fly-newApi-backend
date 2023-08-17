package com.flyCommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flyCommon.model.entity.InterfaceInfoNew;


public interface InnerInterfaceInfoNewService  {


    /**
     * 查询模拟接口是否存在(路径(ip+端口+接口名)，方法，参数，布尔)
     */
    InterfaceInfoNew getInterfaceInfo(String path, String method, String userRequestParams);

}
