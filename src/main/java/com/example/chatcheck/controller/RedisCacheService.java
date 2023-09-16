package com.example.chatcheck.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 从Redis缓存中读取数据
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写数据到Redis缓存中
     *
     * @param key      缓存key
     * @param value    缓存值
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        if (ObjectUtil.isNotEmpty(value)) {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
    }

    /**
     * 删除Redis缓存中的数据
     *
     * @param key 缓存key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除Redis缓存中的数据
     *
     * @param key 缓存key
     */
    public void set(String key, String value) {
//        if(key.contains("client")||key.contains("pue")||key.contains("hasClient")){
//            System.out.println("hasClient"+JSONArray.toJSONString(redisTemplate.keys("hasClient*")));
//            System.out.println("client忙碌"+JSONArray.toJSONString(redisTemplate.keys("client*")));
//            System.out.println("pue忙碌"+JSONArray.toJSONString(redisTemplate.keys("pue*")));
//        }
        set(key, value,60,TimeUnit.SECONDS);
    }

    public void setNoDel(String key, String value) {
        if (ObjectUtil.isNotEmpty(value)) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean hasKeyCount(String key) {
        return redisTemplate.keys("que*").size()>0;
    }

    public Set<String> keys(String key) {
        Set<String> keys = redisTemplate.keys(key);
        return keys;
    }


}