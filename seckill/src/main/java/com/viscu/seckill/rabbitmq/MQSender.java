package com.viscu.seckill.rabbitmq;

import com.viscu.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 描述
 */

@Service
public class MQSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    //使用Direct模式交换机 进行异步处理订单
    public void sendSeckillMessage(SeckillMessage message) {
        String msg = RedisService.objectToJson(message);
        LOGGER.info("send message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.SEC_KILL_QUEUE, msg);
    }


    //发送消息
    public void send(Object message){
        String msg = RedisService.objectToJson(message);
        LOGGER.info("send message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.MESSAGE_QUEUE, msg);
    }

    public void sendTopic(Object message){
        String msg = RedisService.objectToJson(message);
        LOGGER.info("send topic message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGER, "topic.key1", msg+"1"); //q1 q2都可以匹配到
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGER, "topic.key2", msg+"2"); //q2匹配到
    }

    public void sendFanout(Object message){
        String msg = RedisService.objectToJson(message);
        LOGGER.info("send fanout message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGER, "", msg);
    }

    public void sendHeader(Object message){
        String msg = RedisService.objectToJson(message);
        LOGGER.info("send header message: "+msg);
        //加上头部信息
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "v1");
        properties.setHeader("header2", "v2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGER, "", obj);
    }
}
