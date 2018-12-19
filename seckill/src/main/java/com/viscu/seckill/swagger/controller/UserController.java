package com.viscu.seckill.swagger.controller;

import com.viscu.seckill.swagger.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-19
 * @ 描述
 */

@Api(value = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation(value = "获取单个用户", notes = "传入id获取单个用户")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String user(@ApiParam(value = "用户Id", required = true)@PathVariable Long id){
        return "user id: " + id;
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List list() {
        List list = new ArrayList();
        list.add("Sam1");
        list.add("Sam2");
        list.add("Sam3");
        return list;
    }

    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "新建用户", notes = "新建一个用户")
//    @ApiImplicitParams({
    //注意：paramType需要指定为body
//            @ApiImplicitParam(name = "user", value = "用户数据", required = true, paramType = "body", dataType = "User")
//    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@ApiParam(value = "用户数据", required = true) @RequestBody User user) {
        System.out.println("user : " + user.getName() + " " + user.getAge());
        return "success 新建user : " + user.getName() + " " + user.getAge();
    }


    /**
     * 删除用户
     *
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "通过用户id删除用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "Long")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        System.out.println("删除用户：" + id);
        return "success 删除user" + id;
    }


    /**
     * 更新用户
     *
     * @return
     */
    @ApiOperation(value = "更新用户", notes = "更新已存在用户")
    @ApiImplicitParam(name = "user", value = "用户数据", required = true, paramType = "body", dataType = "User")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public String update(@RequestBody User user) {
        System.out.println("更新用户：" + user.getId() + " " + user.getName() + " " + user.getAge());
        return "success 更新user : " + user.getId() + " " + user.getName() + " " + user.getAge();
    }



}
