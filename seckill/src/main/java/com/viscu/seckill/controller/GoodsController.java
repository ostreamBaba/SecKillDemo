package com.viscu.seckill.controller;

import com.viscu.seckill.domain.SkUser;
import com.viscu.seckill.redis.GoodsKey;
import com.viscu.seckill.redis.RedisService;
import com.viscu.seckill.result.Result;
import com.viscu.seckill.service.GoodsService;
import com.viscu.seckill.vo.GoodsDetailVo;
import com.viscu.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 优化技术： 页面缓存+url缓存+对象缓存 注意缓存的一致性
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private RedisService redisService;

    //访问cookie
    //多台服务器 不通过session来取值 通过token+redis来取 单点登录系统
    /*@RequestMapping("/to_list")
    public String toList(HttpServletResponse response, Model model, @CookieValue(value = SkUserService.COOKIE_NAME_TOKEN, required = false)String cookieToken,
                         @RequestParam(value = SkUserService.COOKIE_NAME_TOKEN, required = false) String paramToken){
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        SkUser user = skUserService.getByToken(response, token);
        model.addAttribute("user", user);
        return "goods_list";
    }*/

    //通过对页面进行缓存到redis中 加大QPS
    //防止thymeleafViewResolver多次对页面进行渲染
    //而且缓存的时间不能太长 保证数据的及时性
    //把获取token的值的工作单独封装到UserArgumentResolver中去 直接返回一个SkUser对象
    @RequestMapping("/to_list")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, SkUser user, Model model){
        //页面缓存 将thymeleaf 缓存到redis中
        //取缓存
        //只有一个页面
        String html = redisService.get(GoodsKey.getGoodsList,"", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";
        //手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set( GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

/*    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public String toDetail(HttpServletRequest request, HttpServletResponse response, Model model, SkUser user, @PathVariable("goodsId")
                           Long goodsId){
        //页面缓存 将thymeleaf 缓存到redis中
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //snowflake算法 商品id
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        //System.out.println(startAt);
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //System.out.println(now);
        //秒杀的状态
        int seckillStatus = 0;
        //倒计时
        int remainSeconds = 0;
        if(now < startAt){ //秒杀没开始
            seckillStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);
            System.out.println(remainSeconds);
        }else if(now > endAt){ //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else { //秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";

        //手动渲染
        //不同的页面有不同的详情
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set( GoodsKey.getGoodsDetail, ""+goodsId, html );
        }
        return html;
    }*/


    @RequestMapping("/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(SkUser user, @PathVariable("goodsId")
            Long goodsId){
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0;
        //倒计时
        int remainSeconds = 0;
        if(now < startAt){ //秒杀没开始
            seckillStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);
            System.out.println(remainSeconds);
        }else if(now > endAt){ //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else { //秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setSkUser(user);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }


}
