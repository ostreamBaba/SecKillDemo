package com.viscu.seckill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * @描述 获取某个对象
     * @param prefix
     * @param key
     * @param clazz
     * @return T
     * @create by ostreamBaba on 上午3:31 18-12-16
     */

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            String json = jedis.get(realKey);
            return jsonToBean(json, clazz);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * @描述 设置某个对象
     * @param prefix
     * @param key
     * @param value
     * @return boolean
     * @create by ostreamBaba on 上午3:31 18-12-16
     */

    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String json = objectToJson(value);
            if(json == null || json.length() <= 0){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            int expire = prefix.expireSeconds();
            if(expire <= 0){
                jedis.set(realKey, json);
            }else {
                jedis.setex(realKey, expire, json);
            }

            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    //判断是否存在
    public boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    //增加
    public Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    //减少
    public Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String objectToJson(T value){
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return value+"";
        }else if(clazz == String.class){
            return (String) value;
        }else if(clazz == long.class || clazz == Long.class){
            return value+"";
        }
        else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        if(json == null || json.length() <= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(json);
        }else if(clazz == String.class){
            return (T) json;
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(json);
        }else{
            return JSON.toJavaObject(JSON.parseObject(json), clazz);
        }

    }

    private void returnToPool(Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }

    public boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix()+key;
            long res = jedis.del(realKey);
            return res > 0;
        }finally {
            returnToPool(jedis);
        }
    }


}
