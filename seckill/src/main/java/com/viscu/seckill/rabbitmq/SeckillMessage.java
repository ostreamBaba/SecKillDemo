package com.viscu.seckill.rabbitmq;

import com.viscu.seckill.domain.SkUser;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 描述
 */
public class SeckillMessage {

    private SkUser user;

    private long goodsId;

    public SkUser getUser() {
        return user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setUser(SkUser user) {
        this.user = user;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
