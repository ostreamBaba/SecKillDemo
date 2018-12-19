package com.viscu.seckill.redis;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

public class UserKey extends BasePrefix{

    private static final int TOKEN_EXPIRE = 3600*24*2;

    private UserKey(int expireSeconds, String prefix) {
        super( expireSeconds, prefix );
    }

    private UserKey(String prefix) {
        super( prefix );
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");

}
