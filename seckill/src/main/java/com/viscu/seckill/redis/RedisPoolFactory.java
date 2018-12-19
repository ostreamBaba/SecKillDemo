package com.viscu.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Component
public class RedisPoolFactory {

    //注入配置文件
    @Autowired
    RedisConfig redisConfig;

    //通过Bean
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getPoolMaxTotal());
        config.setMaxIdle(redisConfig.getPoolMaxIdle());
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);

        JedisPool jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout()*1000);
        return jedisPool;

    }

}
