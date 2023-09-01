package com.fly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.exception.BusinessException;
import com.fly.service.OrderApiService;
import com.fly.mapper.OrderApiMapper;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.OrderApi;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.entity.UserInterfaceInfo;
import com.flyCommon.model.request.Order.OrderRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @description 针对表【order_api(订单)】的数据库操作Service实现
 * @createDate 2023-08-25 17:43:00
 */
@Service
public class OrderApiServiceImpl extends ServiceImpl<OrderApiMapper, OrderApi>
        implements OrderApiService {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private InterfaceInfoServiceImpl interfaceInfoService;
    @Resource
    private UserInterfaceInfoServiceImpl userInterfaceInfoService;

    @Override
    public boolean addOrder(OrderRequest orderRequest) {
        if (orderRequest == null || orderRequest.getUserId() == null || orderRequest.getInterfaceInfoId() == null || orderRequest.getBuyCount() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(orderRequest.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        InterfaceInfoNew interfaceInfo = interfaceInfoService.getById(orderRequest.getInterfaceInfoId());
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        double infoPrice = interfaceInfo.getPrice();

        Integer buyCount = orderRequest.getBuyCount();
        if (buyCount <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "购买次数不能小于0");
        }

        // 总价钱
        double totalMoney = buyCount * infoPrice;
        if (totalMoney != 0 && totalMoney != orderRequest.getTotalMoney()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总价错误");
        }

        // 获取当前时间
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        // 将时间增加10分钟
        calendar.add(Calendar.MINUTE, 10);
        // 获取增加后的时间
        Date newTime = calendar.getTime();

        // 生成订单
        OrderApi order = new OrderApi();
        BeanUtils.copyProperties(orderRequest, order);
        order.setTotalMoney(totalMoney);
        order.setPrice(infoPrice);
        order.setStatus(1);
        order.setDelayTime(newTime);

        order.setOrderSn(orderRequest.getUserId() + orderRequest.getInterfaceInfoId() + "" + System.currentTimeMillis());

        synchronized (order.getUserId()) {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId",orderRequest.getUserId());
            queryWrapper.eq("interfaceInfoId",orderRequest.getInterfaceInfoId());
            userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
            userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum()+buyCount);
            userInterfaceInfoService.updateById(userInterfaceInfo);

            // 判断余额是否充足
            boolean save = this.save(order);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "购买失败");
            }
        }
        return true;
    }

    @Override
    public OrderApi getOrderById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderApi order = this.getById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "找不到订单");
        }
        return order;
    }

    @Override
    public boolean deleteOrderById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = this.removeById(id);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return b;
    }

    @Override
    public List<OrderApi> getListOrderByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<OrderApi> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        return this.list(queryWrapper);
    }

    @Override
    public List<OrderApi> getListAllOrderAdmin() {
        return this.list();
    }

    @Override
    public boolean cancelOrder(Integer orderId) {
        if (orderId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderApi orderApi = this.getById(orderId);
        orderApi.setStatus(2);
        return this.updateById(orderApi);
    }
}
