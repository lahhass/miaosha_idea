package com.lahhass.miaosha.dao;

import com.lahhass.miaosha.domain.Goods;
import com.lahhass.miaosha.domain.MiaoshaGoods;
import com.lahhass.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id=#{goodsId} and stock_count > 0") //解决卖超问题
    public int reduceStock(MiaoshaGoods g);

    @Update("update miaosha_goods set stock_count=#{stockCount} where goods_id=#{goodsId}")
    void resetStock(MiaoshaGoods g);
}
