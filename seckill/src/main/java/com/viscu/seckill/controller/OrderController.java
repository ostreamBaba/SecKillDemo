package com.viscu.seckill.controller;

import com.viscu.seckill.domain.OrderInfo;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.result.CodeMsg;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.GoodsService;
import com.viscu.seckill.service.OrderService;
import com.viscu.seckill.vo.GoodsVo;
import com.viscu.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ Create by ostreamBaba on 18-12-17
 * @ 描述
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, SkUser user,
                                      @RequestParam("orderId")long orderId){
        if(user == null){
            return Result.error( CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setOrderInfo(orderInfo);
        return Result.success(detailVo);
    }


}
