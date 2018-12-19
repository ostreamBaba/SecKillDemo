package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 描述
 */
public class SeckillKey extends BasePrefix{

    public static final Integer SEC_KILL_KEY_EXPIRE = 3600*24*2;

    public SeckillKey(String prefix) {
        super( prefix );
    }

    public SeckillKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    public static SeckillKey isGoodsOver = new SeckillKey(SEC_KILL_KEY_EXPIRE, "go");

    public static SeckillKey getSeckillPath = new SeckillKey(60, "mp");

    public static SeckillKey getSeckillVerifyCode = new SeckillKey(300, "vc");

}
