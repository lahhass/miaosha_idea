package com.lahhass.miaosha.rabbitmq;

import com.lahhass.miaosha.domain.MiaoshaOrder;
import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.GoodsService;
import com.lahhass.miaosha.service.MiaoshaService;
import com.lahhass.miaosha.service.OrderService;
import com.lahhass.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        log.info("receive message:" +message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        //查数据库判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了，不能重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单 秒杀成功后进入订单详情页
        miaoshaService.miaosha(user, goods);

    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message){
//        log.info("receive message:" +message);
//    }
//
//    /**
//     * Topic模式 Exchange交换机：数据发送到交换机上，交换机把数据发到queue，做了一个路由
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message){
//        log.info("topic queue1 message:" +message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message){
//        log.info("topic queue2 message:" +message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaders(byte[] message){
//        log.info("headers queue message:" +new String(message));
//    }



}
