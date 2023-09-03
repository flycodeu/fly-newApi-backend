package com.fly.Task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.service.impl.OrderApiServiceImpl;
import com.flyCommon.model.entity.OrderApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 修改过期订单时间
 */
@Component
public class OrderExpirationTask {

    @Autowired
    private OrderApiServiceImpl orderService;

    @Scheduled( fixedRate = 60000 ) // 每分钟执行一次
    public void checkOrderExpiration() {
        // 获取当前时间
        orderService.list(new QueryWrapper<OrderApi>().eq("status", 0)).stream().forEach(orderApi -> {
            Date delayTime = orderApi.getDelayTime();
            Date now = new Date();
            // 如果当前时间在延迟时间之后，将订单状态设置为4
            if (now.after(delayTime)) {
                orderApi.setStatus(4);
                orderService.updateById(orderApi);
            }
        });

    }
}