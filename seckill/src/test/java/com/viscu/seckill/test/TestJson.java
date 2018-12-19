package com.viscu.seckill.test;

import com.alibaba.fastjson.JSON;
import com.viscu.seckill.domain.SkUser;
import org.junit.Test;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */
public class TestJson {

    public <T> void testJson(T value){
        String json = JSON.toJSONString(value);
        System.out.println(json);
    }


    @Test
    public void test(){
        SkUser skUser = new SkUser();
        skUser.setPassword("1231");
        skUser.setId(123L);
        System.out.println(objectToJson(skUser));
    }

    private <T> String objectToJson(T value){
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return value+"";
        }else if(clazz == String.class){
            return (String) value;
        }else if(clazz == long.class || clazz == Long.class){
            return value+"";
        }
        else {
            return JSON.toJSONString(value);
        }
    }

}
