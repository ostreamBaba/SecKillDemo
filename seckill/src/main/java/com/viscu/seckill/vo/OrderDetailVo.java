package com.viscu.seckill.vo;

import com.viscu.seckill.domain.OrderInfo;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */
public class OrderDetailVo {

    private GoodsVo goodsVo;
    private OrderInfo orderInfo;

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
