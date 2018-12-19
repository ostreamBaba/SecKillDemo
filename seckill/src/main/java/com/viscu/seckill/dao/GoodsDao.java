package com.viscu.seckill.dao;

import com.viscu.seckill.domain.Goods;
import com.viscu.seckill.domain.SeckillGoods;
import com.viscu.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */

@Mapper
public interface GoodsDao {

    @Select("SELECT g.*, sg.stock_count ,sg.start_date, sg.end_date, sg.seckill_price FROM seckill_goods sg LEFT JOIN goods g ON sg.goods_id=g.id")
    List<GoodsVo> getGoodsVo();

    @Select("SELECT g.*, sg.stock_count ,sg.start_date, sg.end_date, sg.seckill_price FROM seckill_goods sg LEFT JOIN goods g ON sg.goods_id=g.id WHERE g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") Long goodsId);

    //stock_count > 0 防止库存超卖 通过数据库保证超卖
    @Update("UPDATE seckill_goods SET stock_count=stock_count-1 WHERE goods_id=#{goodsId} AND stock_count>0")
    int reduceStock(SeckillGoods goodsId);

    @Select("SELECT * FROM goods WHERE id = #{id} AND goods_name=#{name}")
    Goods get(@Param("id") long id, @Param("name") String name);
}
