package org.HJan.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisUtils {
    private static RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public RedisUtils(RedisTemplate<String,Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 重置redis中的数据
     */
    public static void loadData(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
