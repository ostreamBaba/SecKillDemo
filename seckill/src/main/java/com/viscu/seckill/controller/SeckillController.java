package com.viscu.seckill.controller;

import com.viscu.seckill.access.AccessLimit;
import com.viscu.seckill.domain.SeckillOrder;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.rabbitmq.MQSender;
import com.viscu.seckill.rabbitmq.SeckillMessage;
import com.viscu.seckill.redis.GoodsKey;
import com.viscu.seckill.redis.RedisService;

import com.viscu.seckill.result.CodeMsg;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.GoodsService;
import com.viscu.seckill.service.OrderService;
import com.viscu.seckill.service.SeckillService;
import com.viscu.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    //优化判断是否结束
    private volatile Map<Long, Boolean> localOverMap = new HashMap<>();

    //启动 系统初始化的时候调用该接口
    //将商品库存缓存到redis中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        if(goodsList == null){
            return;
        }
        for (GoodsVo goodsVo: goodsList){
            redisService.set( GoodsKey.getSeckillGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /*@RequestMapping("/do_seckill")
    public String doSecKill(Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if(stock == 0){
            model.addAttribute("err_msg", CodeMsg.SEC_KILL_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            model.addAttribute("err_msg", CodeMsg.REPEATE_SEC_KILL.getMsg());
            return "seckill_fail";
        }
        //减库存 下订单 写入订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }*/

    /*//页面静态化
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doSecKill(Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品 req1 req2同时生产两个订单
        int stock = goods.getStockCount();
        System.out.println("库存还有: "+stock);
        if(stock == 0){
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_SEC_KILL);
        }
        //减库存 下订单 写入订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }*/


    //压测用  用户id随机生成 超卖问题解决
    /*@RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doSecKill(Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            user = new SkUser();
            Random random = new Random();
            user.setId(10000000000L+random.nextInt(10000)+132);
            //return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品 req1 req2同时生产两个订单
        int stock = goods.getStockCount();
        System.out.println("库存还有: "+stock);
        if(stock == 0){
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_SEC_KILL);
        }
        //减库存 下订单 写入订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }*/


    private static final Object stockLock = new Object();

    //压测用 用户id随机生成 超卖问题解决
    //页面静态化 优化
    /*@RequestMapping(value = "/{path}/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSecKill(@PathVariable("path")String path, Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            user = new SkUser();
            Random random = new Random();
            user.setId(10000000000L+random.nextInt(10000)+132);
        }
        //验证path
        boolean check = seckillService.checkPath(path, user, goodsId);
        if(check){
            return Result.error( CodeMsg.REQUEST_ILLEGAL);
        }

        //利用内存标记 减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }

        //判断库存
        //锁控制 保证redis的自减为0
        synchronized (stockLock){
            long stock = redisService.get(GoodsKey.getSeckillGoodsStock, ""+goodsId, long.class);
            if(stock <= 0){
                localOverMap.put(goodsId, true);
                return Result.error(CodeMsg.SEC_KILL_OVER);
            }
            redisService.decr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
        }

        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            redisService.incr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
            return Result.error(CodeMsg.REPEATE_SEC_KILL);
        }
        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(message);
        return Result.success(0); //0代表排队中
    }*/

    /*//页面静态化 优化
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSecKill(Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //利用内存标记 减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }

        //判断库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
        if(stock < 0){
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            redisService.incr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
            return Result.error(CodeMsg.REPEATE_SEC_KILL);
        }
        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(message);
        return Result.success(0); //0代表排队中
    }*/


    //隐藏秒杀接口
    //先调用path生产一个随机的path
    //然后调用该秒杀接口
    @RequestMapping(value = "/{path}/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSecKill(@PathVariable("path")String path, Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = seckillService.checkPath(path, user, goodsId);
        if(!check){
            return Result.error( CodeMsg.REQUEST_ILLEGAL);
        }

        //利用内存标记 减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SEC_KILL_OVER);
        }

        //判断库存
        //锁控制 保证redis的自减为0
        synchronized (stockLock){
            long stock = redisService.get(GoodsKey.getSeckillGoodsStock, ""+goodsId, long.class);
            if(stock <= 0){
                localOverMap.put(goodsId, true);
                return Result.error(CodeMsg.SEC_KILL_OVER);
            }
            redisService.decr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
        }

        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            redisService.incr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
            return Result.error(CodeMsg.REPEATE_SEC_KILL);
        }
        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(message);
        return Result.success(0); //0代表排队中
    }


    // orderId: 秒杀成功 -1 失败 0 排队中
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> SecKillResult(Model model, SkUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }

    //实现通用的限流
    //使用注解+拦截器来限流
    @AccessLimit(seconds=5, maxCount=5)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSecKillPath(Model model, SkUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0")int verifyCode){
        try {
            model.addAttribute("user", user);
            if(user == null){
                return Result.error(CodeMsg.SESSION_ERROR);
            }
            //验证码校验
            boolean check = seckillService.checkVerifyCode(user, goodsId, verifyCode);
            if(!check){
                return Result.error(CodeMsg.REQUEST_ILLEGAL);
            }
            String path = seckillService.createSecKillPath(user, goodsId);
            return Result.success(path);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }
     /*//查询访问次数 5秒钟访问5次
            String uri = request.getRequestURI();
            String key = uri + "_" + user.getId();
            Integer count = redisService.get(AccessKey.access, key, Integer.class);
            //窗口抖动太大 增加限流窗口
            if(count == null){
                redisService.set(AccessKey.access, key, 1);
            }else if(count < 5){
                redisService.incr(AccessKey.access, key);
            }else {
                //超过五次
                return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
            }*/


    //图片验证码
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSecKillVerifyCode(HttpServletResponse response, SkUser user, @RequestParam("goodsId")long goodsId){
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage image = seckillService.createVerifyCode(user, goodsId);
        try {
            //数据通过outputStream输出出去
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "JPEG" ,outputStream);
            outputStream.flush();
            outputStream.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SEC_KILL_FAIL);
        }

    }

}
