package com.fly.manager;

import com.fly.common.ErrorCode;
import com.fly.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/*
专门提供RedisLimiter限流基础服务，提供统一能力
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;


    /**
     * @param key 区分不同得限流器，比如不同得用户id
     */
    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了，请求一个令牌 , 这里可以改变，普通用户每秒5个令牌，vip用户每秒一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "请求次数过多，稍后再试");
        }

    }
}
