package com.viscu.seckill.access;

import java.lang.annotation.*;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 限流注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    int seconds() default 0;

    int maxCount() default 0;

    boolean needLogin() default true;

}
