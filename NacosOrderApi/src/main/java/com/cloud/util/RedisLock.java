package com.cloud.util;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 在分布式系统中，多个进程或线程可能会同时访问共享资源，为了避免并发冲突，需要使用分布式锁
 */
public class RedisLock {

    private static final long LOCK_EXPIRE_TIME = 1000 * 3; //3秒

    @Resource
    private RedisTemplate redisTemplate;

    //获取锁
    public boolean acquireLock(String lockKey, String requestId) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, LOCK_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return result != null && result;

    }

    //释放锁
    public void releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            return connection.eval(script.getBytes(),
                    ReturnType.INTEGER,
                    1,
                    lockKey.getBytes(),
                    requestId.getBytes());
        });
    }

    //使用案例

    public void doSomeThing() {
        String lockKey = "lockKey";
        String requestId = UUID.randomUUID().toString();
        if (acquireLock(lockKey, requestId)) {
            try {
                //TODO do something
            } catch (Exception e) {
                releaseLock(lockKey, requestId);
            }
        }
    }
}
