package com.viscu.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ RabbitMQ配置
 */

@Configuration
public class MQConfig {

    public static final String SEC_KILL_QUEUE = "sec_kill_queue";

    public static final String MESSAGE_QUEUE = "message_queue";

    public static final String TOPIC_QUEUE_1 = "topic_queue_1";

    public static final String TOPIC_QUEUE_2 = "topic_queue_2";

    public static final String TOPIC_EXCHANGER = "topic_exchange";

    public static final String ROUNTING_KEY1 = "topic.key1";

    public static final String ROUNTING_KEY2 = "topic.#";

    public static final String FANOUT_QUEUE_1 = "fanout_queue_1";

    public static final String FANOUT_QUEUE_2 = "fanout_queue_2";

    @Bean
    public Queue secKillQueue(){
        return new Queue(SEC_KILL_QUEUE, true);
    }

    @Bean
    public Queue queue(){
        return new Queue(MESSAGE_QUEUE, true);
    }

    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE_1, true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE_2, true);
    }

    @Bean
    public Queue fanoutQueue1(){
        return new Queue(FANOUT_QUEUE_1, true);
    }

    @Bean
    public Queue fanoutQueue2(){
        return new Queue(FANOUT_QUEUE_2, true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGER);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUNTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUNTING_KEY2);
    }

    //Fanout模式 广播模式

    public static final String FANOUT_EXCHANGER = "fanout_exchange";

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGER);
    }

    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    public static final String HEADERS_EXCHANGER = "headers_exchange";

    //Header模式

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGER);
    }

    public static final String HEADER_QUEUE = "header_queue";

    @Bean
    public Queue headerQueue1(){
        return new Queue(HEADER_QUEUE, true);
    }

    @Bean
    public Binding headerBinding(){
        //满足map的所有kv
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "v1");
        map.put("header2", "v2");
        return BindingBuilder.bind(headerQueue1()).to(headersExchange())
                .whereAll(map).match();
    }

}
