package com.viscu.seckill.controller;


import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.SkUserService;
import com.viscu.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired(required = false)
    private SkUserService skUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/do_login", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo, HttpServletResponse response){
        logger.info(loginVo.toString());
        skUserService.login(response, loginVo);
        return Result.success(true);
    }

    //参数校验 利用jsr303进行参数验证
        /*String passInput = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if(StringUtils.isEmpty(passInput)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登录
        CodeMsg cm = skUserService.login(loginVo);
        if(cm.getCode() == 0){
            return Result.success(CodeMsg.SUCCESS);
        }else {
            return Result.error(cm);
        }*/


}
