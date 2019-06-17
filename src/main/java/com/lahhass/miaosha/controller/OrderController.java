package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.domain.OrderInfo;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.GoodsService;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.service.OrderService;
import com.lahhass.miaosha.vo.GoodsVo;
import com.lahhass.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;



    //有时候手机端不会把token放入cookie，直接作为参数传
    //首先先从param中取token，取不到再从cookie中取

    @RequestMapping("/detail")
    @ResponseBody
    //可以加@NeedLogin拦截器
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
           @RequestParam("orderId") long orderId) {

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        return Result.success(vo);
    }


}
