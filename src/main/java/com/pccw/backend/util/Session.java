package com.pccw.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Session
 */

 @Component
 @Slf4j
public class Session<T> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    HttpServletRequest request;
    
    /**
     * 存
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
    /**
     * 取
     * @param <T>
     * @param key
     * @return
     */
    public <T> T get(String key) {
        try {
            return (T)redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    /**
     * 删
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    /**
     * 判
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            return Objects.isNull(obj)?false:true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 是否完成登录
     * @return
     */
    public boolean isLogin(String key){
        return hasKey(key);
    }
    /**
     * 是否拥有权限
     * @return
     */
    public boolean hasRight(String module,String ctrl){
        return true;
    }
    /**
     * 获取Session用户对象
     * @return
     */
    public <T> T getUser(){
        return this.<T>get(request.getHeader("TOKEN"));
    }

    /**
     * 获取用户TOKEN
     * @return
     */
    public String getToken() {
        return request.getHeader("TOKEN");
    }
    
}