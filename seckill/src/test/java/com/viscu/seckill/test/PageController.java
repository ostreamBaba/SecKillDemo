package com.viscu.seckill.test;

import com.viscu.seckill.domain.User;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.redis.UserKey;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Controller
@RequestMapping("/demo")
public class PageController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/{page}")
    public String toPage(@PathVariable String page){
        return page;
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> doGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/tx")
    @ResponseBody
    public Result<Boolean> tx(){
        boolean result = userService.tx();
        return Result.success(result);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get( UserKey.getById,1+"", User.class);
        return Result.success(user);
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        Boolean flag = redisService.set(UserKey.getById,1+"", user);
        return Result.success(flag);
    }


}
