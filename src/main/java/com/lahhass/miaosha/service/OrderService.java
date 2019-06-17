package com.lahhass.miaosha.service;


import com.lahhass.miaosha.dao.OrderDao;
import com.lahhass.miaosha.domain.MiaoshaOrder;
import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.domain.OrderInfo;
import com.lahhass.miaosha.redis.OrderKey;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



@Service
public class OrderService {


    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        //return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId,MiaoshaOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    //小优化：生成订单后存入缓存
    //orderinfo和miaosha_order
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        //orderinfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0); //最好用枚举
        orderInfo.setUserId(user.getId());
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String createDate = df.format(orderInfo.getCreateDate());
        orderDao.insert(orderInfo); //插入后mybatis会将生成的id塞进对象里
        //miaosha_order
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        //orderInfo.setId(orderId);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goods.getId(), miaoshaOrder);

        return orderInfo;
    }


    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMiaoshaOrders();
    }
}
