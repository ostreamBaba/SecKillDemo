package com.viscu.seckill.dao;

import com.viscu.seckill.domain.OrderInfo;
import com.viscu.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
@Mapper
public interface OrderDao {

    @Select("SELECT * FROM seckill_order WHERE user_id=#{userId} AND goods_id=#{goodsId}")
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(@Param("userId") long userId,
                                                   @Param("goodsId") long goodsId);

    //插入并获取id
    @Insert("INSERT INTO order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date, pay_date)" +
            "VALUES(#{userId},#{goodsId},#{goodsName},#{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate},#{payDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "SELECT last_insert_id()")
    int insert(OrderInfo orderInfo);
    //SelectKey 记载 插入的自增id已经注入到我们插入的bean中 而不是返回值中

    @Insert("INSERT INTO seckill_order(user_id, order_id, goods_id)VALUES(#{userId},#{orderId},#{goodsId})")
    int insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("SELECT * FROM order_info WHERE id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);
}
