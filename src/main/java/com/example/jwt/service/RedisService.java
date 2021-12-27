package com.example.jwt.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务工具类
 *
 * @author : Charles
 * @date : 2021/12/10
 */
@Service
public class RedisService {
    @Resource
    private RedisTemplate<Serializable, Object> redisTemplate;
    /**
     * 读取缓存
     */
    public Object get(String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
    /**
     * 判断缓存中是否存在
     */
    public boolean exists(String key) {
        return StringUtils.hasLength(key) && Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    /**
     * 删除缓存
     */
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 写入缓存
     */
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存 并 加上过期时间（毫秒）
     */
    public boolean set(String key, Object value, Long expireTimeMillis) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTimeMillis, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入过期时间（毫秒）
     */
    public boolean expire(String key, Long expireTimeMillis) {
        boolean result = false;
        try {
            redisTemplate.expire(key, expireTimeMillis, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
