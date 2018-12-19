package com.viscu.seckill.service;

import com.viscu.seckill.dao.GoodsDao;
import com.viscu.seckill.dao.OrderDao;
import com.viscu.seckill.domain.OrderInfo;
import com.viscu.seckill.domain.SeckillOrder;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.OrderKey;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class OrderService {

    @Autowired(required = false)
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    //利用缓存判断用户是否已经重复抢了
    /*public SeckillOrder getSeckillOrderByUserIdAndGoodsId(long id, long goodsId) {
        return orderDao.getSeckillOrderByUserIdAndGoodsId(id, goodsId);
    }*/

    public SeckillOrder getSeckillOrderByUserIdAndGoodsId(long userId, long goodsId) {
        return redisService.get( OrderKey.getSeckillOrderByUidGid,""+userId+"_"+goodsId, SeckillOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }


    //利用数据库的唯一索引保证 一个用户只有一个秒杀order u_uid_gid(user_id, goods_id)
    @Transactional
    public OrderInfo createOrder(SkUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        long orderId = orderInfo.getId();
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        //将抢到订单写入缓存
        redisService.set(OrderKey.getSeckillOrderByUidGid,""+user.getId()+"_"+goods.getId(), seckillOrder);
        return orderInfo;
    }
}
