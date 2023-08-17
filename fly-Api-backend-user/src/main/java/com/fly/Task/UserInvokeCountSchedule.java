package com.fly.Task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.service.InterfaceInfoService;
import com.fly.service.UserInterfaceInfoService;
import com.fly.service.UserService;
import com.fly.utils.RedisConstants;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.User;
import com.flyCommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务
 */
@Component
@Slf4j
public class UserInvokeCountSchedule {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Boolean> redisTemplate;

//    @Scheduled( cron = "0/1 * * * * ? " )
    public void userInvokeCount() {
        List<InterfaceInfoNew> interfaceInfoNewList = interfaceInfoService.list();
        List<User> userList = userService.list();

        ValueOperations<String, Boolean> valueOperations = redisTemplate.opsForValue();

        for (User user : userList) {
            for (InterfaceInfoNew interfaceInfoNew : interfaceInfoNewList) {
                String cacheKey = RedisConstants.SCHEDULED_TASK + user.getId() + ":" + interfaceInfoNew.getId();
                Boolean recordExists = valueOperations.get(cacheKey);

                if (recordExists == null || !recordExists) {
                    UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
                    userInterfaceInfo.setUserId(user.getId());
                    userInterfaceInfo.setInterfaceInfoId(interfaceInfoNew.getId());
                    userInterfaceInfo.setLeftNum(interfaceInfoNew.getInvokeCount());
                    userInterfaceInfo.setTotalNum(interfaceInfoNew.getInvokeCount());
                    // 保存数据库
                    userInterfaceInfoService.save(userInterfaceInfo);
                    // 将记录添加到缓存
                    valueOperations.set(cacheKey, true);
                }
            }
        }
    }
}