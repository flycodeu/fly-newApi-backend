package com.fly.service.impl.Inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.ErrorCode;
import com.fly.exception.BusinessException;
import com.fly.service.InterfaceInfoService;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.service.InnerInterfaceInfoNewService;
import com.flyCommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部调用
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoNewService {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 判断接口是否正确
     *
     * @param path
     * @param method
     * @param userRequestParams
     * @return
     */
    @Override
    public InterfaceInfoNew getInterfaceInfo(String path, String method, String userRequestParams) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>();
        if (path.startsWith("http://")) {
            // 分割成最多 4 部分，[http:], [空], [localhost:8123], [api/story/getStory]
            String[] parts = path.split("/", 4);
            if (parts.length >= 3) {
                String ipAddressAndPort = parts[2]; // localhost:8123
                String[] ipAndPort = ipAddressAndPort.split(":");
                if (ipAndPort.length == 2) {
                    String ipAddress = ipAndPort[0];
                    String port = ipAndPort[1];
                    String url = parts.length == 4 ? "/" + parts[3] : "";
                    queryWrapper.eq("IPAddress", ipAddress);
                    queryWrapper.eq("port", port);
                    queryWrapper.eq("url", url);
                    queryWrapper.eq("method", method);
                }
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        InterfaceInfoNew one = interfaceInfoService.getOne(queryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return one;
    }
}
