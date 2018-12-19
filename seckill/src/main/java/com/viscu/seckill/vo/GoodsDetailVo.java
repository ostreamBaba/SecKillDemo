package com.viscu.seckill.vo;

import com.viscu.seckill.domain.SkUser;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */
public class GoodsDetailVo {

    private int seckillStatus = 0;
    private int RemainSeconds = 0;
    private GoodsVo goodsVo;
    private SkUser skUser;

    public SkUser getSkUser() {
        return skUser;
    }

    public void setSkUser(SkUser skUser) {
        this.skUser = skUser;
    }

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public int getRemainSeconds() {
        return RemainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public void setRemainSeconds(int remainSeconds) {
        RemainSeconds = remainSeconds;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
