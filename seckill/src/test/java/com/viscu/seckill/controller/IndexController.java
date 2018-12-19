package com.viscu.seckill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Create by ostreamBaba on 18-12-19
 * @ 描述
 */

@RestController
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

}
