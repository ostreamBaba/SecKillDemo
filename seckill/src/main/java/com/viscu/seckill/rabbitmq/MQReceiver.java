package com.viscu.seckill.rabbitmq;

import com.viscu.seckill.domain.SeckillOrder;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.service.GoodsService;
import com.viscu.seckill.service.OrderService;
import com.viscu.seckill.service.SeckillService;
import com.viscu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 四种模式
 */

@Service
public class MQReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @RabbitListener(queues = MQConfig.SEC_KILL_QUEUE)
    public void receiveSecKill(String message){
        LOGGER.info("receive message: "+message);
        SeckillMessage msg = RedisService.jsonToBean(message, SeckillMessage.class);
        SkUser user = msg.getUser();
        long goodsId = msg.getGoodsId();
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if(stock == 0){
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            return;
        }
        //减库存 下订单 写入订单
        seckillService.seckill(user, goods);
    }

    //Direct模式 交换机Exchange

    //监听队列
    @RabbitListener(queues = MQConfig.MESSAGE_QUEUE)
    public void receive(String message){
        LOGGER.info("receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_1)
    public void receiveTopic1(String message){
        LOGGER.info("topic queue1 receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_2)
    public void receiveTopic2(String message){
        LOGGER.info("topic queue2 receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE_1)
    public void receiveFanout1(String message){
        LOGGER.info("fanout queue1 receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.FANOUT_QUEUE_2)
    public void receiveFanout2(String message){
        LOGGER.info("fanout queue2 receive message: "+message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeader(byte[] message){
        LOGGER.info("header queue receive message: "+new String(message));
    }

}
