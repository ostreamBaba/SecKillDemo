package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 做页面缓存 且时间不能取太长 考虑数据及时性
 */
public class GoodsKey extends BasePrefix {

    public GoodsKey(String prefix) {
        super( prefix );
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl" );
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(3600*24, "gs");


}
