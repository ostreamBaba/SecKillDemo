package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 描述
 */
public class AccessKey extends BasePrefix{

    private AccessKey(String prefix) {
        super( prefix );
    }

    private AccessKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    //5秒访问5次
    public static AccessKey access = new AccessKey("access");

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds, "access");
    }

    public static AccessKey getAccess(){
        return new AccessKey("access");
    }

}
