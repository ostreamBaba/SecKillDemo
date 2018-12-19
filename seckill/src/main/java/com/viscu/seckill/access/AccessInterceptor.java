package com.viscu.seckill.access;

import com.alibaba.fastjson.JSON;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.AccessKey;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.result.CodeMsg;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.SkUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @ Create by ostreamBaba on 18-12-18
 * @ 添加一个限流的拦截器
 */

@Component
public class  AccessInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private SkUserService skUserService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(handler instanceof HandlerMethod){
           //先从cookie中取用户
           SkUser user = getUser(request, response);
           //获取用户
           UserContext.setUser(user);
           HandlerMethod hm = (HandlerMethod) handler;
           AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
           if(accessLimit == null){
                return true;
           }
           //取注解的seconds
           int seconds = accessLimit.seconds();
           //取注解的maxCount
           int maxCount = accessLimit.maxCount();
           String key = request.getRequestURI();
           //取是否需要登录 默认该请求需要登录
           boolean needLogin = accessLimit.needLogin();
           if(needLogin){
               if(user == null){
                   //把请求发送出去 需要通过response
                   render(response,CodeMsg.SESSION_ERROR);
                   return false;
               }
               key += "_" + user.getId();
           }else {
               //doNothing
               System.out.println("直接进行限流");
           }
           //重构-改善既有代码的设计
           Integer count = redisService.get(AccessKey.access, key, Integer.class);
           AccessKey access = AccessKey.withExpire(seconds);
           if(count == null){
               redisService.set(access, key, 1);
           }else if(count < maxCount){
               redisService.incr(access, key);
           }else {
               render(response, CodeMsg.ACCESS_LIMIT_REACHED);
               return false;
           }
       }
       return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws Exception{
        //设置回复消息的格式以防止乱码
        response.setContentType("application/json;charset=UTF-8");
        //通过字节输出流传输该错误
        OutputStream out = response.getOutputStream();
        String msg = JSON.toJSONString(Result.error(sessionError));
        out.write(msg.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    //从token cookie中取出该用户
    private SkUser getUser(HttpServletRequest request, HttpServletResponse response){
        //从request的参数中取是否有该token
        String paramToken = request.getParameter(SkUserService.COOKIE_NAME_TOKEN);
        //获取cookie
        String cookieToken = getCookieValue(request, SkUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return skUserService.getByToken(response, token);
    }

    //从request获取全部的cookie
    private String getCookieValue(HttpServletRequest request, String cookieName){
        //从request找所有cookie进行匹配
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
