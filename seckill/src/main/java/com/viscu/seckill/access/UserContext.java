package com.viscu.seckill.access;

import com.viscu.seckill.domain.SkUser;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ Session共享实现
 */

public class UserContext {

    //利用ThreadLocal实现多线程安全 防止用户信息
    private static ThreadLocal<SkUser> userThreadLocal = new ThreadLocal<>();

    public static void setUser(SkUser user){
        userThreadLocal.set(user);
    }

    public static SkUser getUser(){
        return userThreadLocal.get();
    }

}
