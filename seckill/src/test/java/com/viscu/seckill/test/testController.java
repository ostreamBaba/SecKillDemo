package com.viscu.seckill.test;

import com.viscu.seckill.dao.OrderDao;
import com.viscu.seckill.domain.*;
import com.viscu.seckill.rabbitmq.MQSender;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.GoodsService;
import com.viscu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */

@Controller
@RequestMapping("/test")
public class testController {

    private Logger logger = LoggerFactory.getLogger(testController.class);

    @Autowired
    private MQSender mqSender;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/del")
    @ResponseBody
    public Result<String> del(){
        try(Jedis jedis=jedisPool.getResource()){}
        return null;
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello, RabbitMQ!!!!!");
        return Result.success("hello");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> mqTopic(){
        mqSender.sendTopic("hello, RabbitMQ!!!!!");
        return Result.success("hello");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> mqFanout(){
        mqSender.sendFanout("hello, RabbitMQ!!!!!");
        return Result.success("hello");
    }

    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> mqHeader(){
        mqSender.sendHeader("hello, RabbitMQ!!!!!");
        return Result.success("hello");
    }

    @Autowired(required = false)
    private OrderDao orderDao;

    @RequestMapping("/info")
    @ResponseBody
    private Result<SkUser> getUser(SkUser user){
        logger.info(String.valueOf(user.getId()));
        return Result.success(user);
    }

    @RequestMapping
    @ResponseBody
    public long get(){
        return orderDao.insertSeckillOrder(new SeckillOrder());
    }


    @Autowired
    private GoodsService goodsService;
    @RequestMapping("/test1")
    @ResponseBody
    public GoodsVo seckill() {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(2L);
        return goods;
    }

}
