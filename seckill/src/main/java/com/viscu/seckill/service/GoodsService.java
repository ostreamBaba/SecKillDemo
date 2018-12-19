package com.viscu.seckill.service;

import com.viscu.seckill.dao.GoodsDao;
import com.viscu.seckill.domain.Goods;
import com.viscu.seckill.domain.SeckillGoods;
import com.viscu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class GoodsService {

    @Autowired(required = false)
    private GoodsDao goodsDao;

    public List<GoodsVo> goodsVoList(){
        return goodsDao.getGoodsVo();
    }


    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        return goodsDao.reduceStock(g) > 0;
    }
}
