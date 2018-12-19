package com.viscu.seckill.util;

import java.util.UUID;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
