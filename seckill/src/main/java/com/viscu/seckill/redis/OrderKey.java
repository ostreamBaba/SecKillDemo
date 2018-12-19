package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
public class OrderKey extends BasePrefix{

    private static final int SECKILL_ORDER_EXPIRE = 3600*24*2;

    public OrderKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey(SECKILL_ORDER_EXPIRE, "SkOrder_Uid_Gid");
}
