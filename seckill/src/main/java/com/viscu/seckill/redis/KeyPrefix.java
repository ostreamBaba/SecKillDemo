package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
