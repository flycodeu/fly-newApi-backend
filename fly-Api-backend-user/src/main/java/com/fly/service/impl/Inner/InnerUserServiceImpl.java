package com.fly.service.impl.Inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.common.ErrorCode;
import com.fly.exception.BusinessException;
import com.fly.service.UserService;
import com.fly.service.impl.InterfaceInfoServiceImpl;
import com.fly.service.impl.UserInterfaceInfoServiceImpl;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    /**
     * 判断yonghuak，sk正确
     *
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUserAkSkValid(String accessKey) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User one = userService.getOne(queryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return one;
    }
}


