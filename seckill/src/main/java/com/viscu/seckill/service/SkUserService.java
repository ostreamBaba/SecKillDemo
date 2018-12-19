package com.viscu.seckill.service;

import com.viscu.seckill.dao.SkUserDao;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.exception.GlobalException;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.redis.SeckillUserKey;
import com.viscu.seckill.redis.UserKey;
import com.viscu.seckill.result.CodeMsg;
import com.viscu.seckill.util.MD5Util;
import com.viscu.seckill.util.UUIDUtil;
import com.viscu.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class SkUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired(required = false)
    private SkUserDao skUserDao;

    @Autowired
    private RedisService redisService;

    public SkUser getById(long id){
        //取缓存
        SkUser skUser = redisService.get(SeckillUserKey.getById, ""+id, SkUser.class);
        if(skUser != null){
            return skUser;
        }
        //取数据库
        skUser = skUserDao.getById(id);
        if(skUser != null){
            redisService.set(SeckillUserKey.getById, ""+id, skUser);
        }
        return skUser;
    }

    //更新 token 删除旧的对象
    public boolean updatePassword(String token, long id, String newPassword){
        //取user
        SkUser user = getById(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        SkUser toBeUpdate = new SkUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassDBPass(newPassword, user.getSalt()));
        skUserDao.update(toBeUpdate);
        //更新数据库成功 更新缓存
        redisService.delete(SeckillUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SeckillUserKey.token, token, user);
        return true;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        try{
            if(loginVo == null){
                throw new GlobalException(CodeMsg.SERVER_ERROR);
            }
            String formPass = loginVo.getPassword();
            String mobile = loginVo.getMobile();
            //判断手机号是否存在
            SkUser user = getById(Long.valueOf(mobile));
            if(user == null){
                throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST); //错误直接抛出异常
            }
            //验证密码
            String dbPass = user.getPassword();
            String dbSalt = user.getSalt();
            String calcPass = MD5Util.formPassDBPass(formPass, dbSalt);
            if(!dbPass.equals(calcPass)){
                throw new GlobalException(CodeMsg.PASSWORD_ERROR);
            }
            //生产cookie
            String token = UUIDUtil.uuid();
            addCookie(response, user, token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void addCookie(HttpServletResponse response, SkUser user, String token){
        redisService.set(UserKey.token, token, user);
        //创建cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置cookie的存活时长
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        //response添加cookie并返回给客户端
        response.addCookie(cookie);
    }

    public SkUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        //延迟有效期
        SkUser user = redisService.get(UserKey.token, token, SkUser.class);
        addCookie(response, user, token );
        return user;
    }
}
