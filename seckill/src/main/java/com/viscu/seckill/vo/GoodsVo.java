package com.viscu.seckill.vo;

import com.viscu.seckill.domain.Goods;

import java.util.Date;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

public class GoodsVo extends Goods{

    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double seckillPrice;

    public Integer getStockCount() {
        return stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }
}
