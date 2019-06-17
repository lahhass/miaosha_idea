package com.lahhass.miaosha.dao;

import com.lahhass.miaosha.domain.MiaoshaOrder;
import com.lahhass.miaosha.domain.OrderInfo;
import com.lahhass.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface OrderDao {


    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Select("select * from order_info where id=#{orderId}")
    public OrderInfo getOrderById(@Param("orderId") long orderId);

    //返回自增主键用SelectKey keyColumn:数据库列名 keyProperty：domain对象属性 before:是否在之前执行
    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)values(" +
            "#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(user_id,order_id,goods_id)values(#{userId},#{orderId},#{goodsId})")
    public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Delete("delete from miaosha_order")
    void deleteMiaoshaOrders();

    @Delete("delete from order_info")
    void deleteOrders();
}
