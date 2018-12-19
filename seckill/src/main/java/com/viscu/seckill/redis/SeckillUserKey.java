package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */
public class SeckillUserKey extends BasePrefix{

    private static final int TOKEN_EXPIRE = 3600*24*2;

    public SeckillUserKey(String prefix) {
        super( prefix );
    }

    public SeckillUserKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE, "tk");

    public static SeckillUserKey getById = new SeckillUserKey("id");
}

