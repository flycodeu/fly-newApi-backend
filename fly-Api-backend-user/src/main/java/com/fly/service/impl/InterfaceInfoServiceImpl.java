package com.fly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fly.common.ErrorCode;
import com.fly.constant.CommonConstant;
import com.fly.exception.BusinessException;
import com.fly.model.entity.InterfaceInfo;
import com.fly.model.entity.InterfaceInfoNew;
import com.fly.model.request.DeleteRequest;
import com.fly.model.request.Interface.InterfaceInfoAddRequest;
import com.fly.model.request.Interface.InterfaceInfoQueryRequest;
import com.fly.model.request.Interface.InterfaceInfoUpdateRequest;
import com.fly.model.vo.UserVO;
import com.fly.service.InterfaceInfoService;
import com.fly.mapper.InterfaceInfoMapper;
import com.fly.service.UserService;
import com.fly.utils.RegexUtils;
import com.fly.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-07-13 18:26:14
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfoNew>
        implements InterfaceInfoService {
    private static final String IPV4_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    @Resource
    private UserService userService;

    @Override
    public Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, String token) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写完整信息");
        }
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfoNew);
        this.validInterfaceInfo(interfaceInfoNew, true);
        UserVO loginUserRedis = userService.getLoginUserRedis(token);
        Long userId = loginUserRedis.getId();
        interfaceInfoNew.setUserId(userId);
        boolean save = this.save(interfaceInfoNew);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建失败");
        }
        return interfaceInfoNew.getId();
    }

    @Override
    public Boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除条件为空");
        }
        Long id = deleteRequest.getId();
        boolean b = this.removeById(id);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public void validInterfaceInfo(InterfaceInfoNew interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String ipAddress = interfaceInfo.getIPAddress();
        Integer port = interfaceInfo.getPort();

        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();

        if (add) {
            if (StringUtils.isAllBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "调用名过长");
        }

        if (StringUtils.isNotBlank(description) && description.length() > 8102) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }

        if (String.valueOf(port).length() > 6 || String.valueOf(port).length() < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "端口号不符合规定");
        }

        if (url == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url不能为空");
        }

        if (!isValidIPAddress(ipAddress)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ip地址不正确");
        }

    }

    @Override
    public Boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        BeanUtils.copyProperties(interfaceInfoNew, interfaceInfoNew);
        // 检验
        validInterfaceInfo(interfaceInfoNew, false);
        Long id = interfaceInfoNew.getId();
        InterfaceInfoNew byId = this.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        boolean b = this.updateById(interfaceInfoNew);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return true;
    }

    @Override
    public InterfaceInfoNew getInterfaceInfoById(Long id) {
        InterfaceInfoNew byId = this.getById(id);
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return this.getById(id);
    }

    @Override
    public List<InterfaceInfoNew> getAllInterfaceInfoByList(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfoNew interfaceInfoNew = new InterfaceInfoNew();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoNew);
        }
        QueryWrapper<InterfaceInfoNew> queryWrapper = new QueryWrapper<>(interfaceInfoNew);
        queryWrapper.orderByDesc("id");
        return this.list(queryWrapper);
    }

    @Override
    public Page<InterfaceInfoNew> getAllInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        InterfaceInfoNew interfaceInfoNew =new InterfaceInfoNew();
        BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfoNew);
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        long current = interfaceInfoQueryRequest.getCurrent();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String name = interfaceInfoQueryRequest.getName();
        ThrowUtils.throwIf(pageSize>20,ErrorCode.FORBIDDEN_ERROR,"请勿爬虫");
        QueryWrapper<InterfaceInfoNew> queryWrapper =new QueryWrapper<>(interfaceInfoNew);
        queryWrapper.like("name",name);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        queryWrapper.orderByDesc("id");
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    /**
     * 正则检验ip地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null)) {
            if (ipAddress.equals("localhost")) {
                return true;
            } else {
                return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
            }
        }
        return false;
    }


}




