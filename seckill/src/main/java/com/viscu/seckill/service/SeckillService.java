package com.viscu.seckill.service;

import com.viscu.seckill.domain.OrderInfo;
import com.viscu.seckill.domain.SeckillOrder;
import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.redis.SeckillKey;
import com.viscu.seckill.util.MD5Util;
import com.viscu.seckill.util.UUIDUtil;
import com.viscu.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(SkUser user, GoodsVo goods) {
        //减库存 下订单 写入订单
       boolean success = goodsService.reduceStock(goods);
       if(success){
           //seckill_order orderInfo
           return orderService.createOrder(user, goods);
       }else {
           setGoodsOver(goods.getId());
       }
       return null;
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
        if(order != null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }

    //秒杀到把值存到redis中表示已经秒杀到
    private void setGoodsOver(Long goodsId) {
        redisService.set( SeckillKey.isGoodsOver,""+goodsId, true);
    }


    public String createSecKillPath(SkUser user, long goodsId) {
        String path = MD5Util.md5( UUIDUtil.uuid()+"123456");
        redisService.set(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId, path);
        return path;
    }

    //检查路径
    public boolean checkPath(String path, SkUser user, long goodsId) {
        if(user == null || path == null){
            return false;
        }
        String oldPath = redisService.get(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId, String.class);
        if(oldPath.equals(path)){
            return true;
        }
        return false;
    }

    public BufferedImage createVerifyCode(SkUser user, long goodsId) {
        if(user == null || goodsId <= 0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color( 100, 59, 86 ));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId, rnd);
        //输出图片
        return image;
    }

    //计算
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //随机生产表达式
    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    //进行验证码验证
    public boolean checkVerifyCode(SkUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId < 0){
            return false;
        }
        Integer oldCode = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId, Integer.class);
        if(oldCode == null || oldCode != verifyCode){
            return false;
        }
        //删除通过的验证码
        redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId);
        return true;
    }
}
