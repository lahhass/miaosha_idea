package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.access.AccessLimit;
import com.lahhass.miaosha.domain.MiaoshaOrder;
import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.domain.OrderInfo;
import com.lahhass.miaosha.rabbitmq.MQSender;
import com.lahhass.miaosha.rabbitmq.MiaoshaMessage;
import com.lahhass.miaosha.redis.*;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.GoodsService;
import com.lahhass.miaosha.service.MiaoshaService;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.service.OrderService;
import com.lahhass.miaosha.util.MD5Util;
import com.lahhass.miaosha.util.UUIDUtil;
import com.lahhass.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 实现InitializingBean后会回调此方法
     * 系统初始化，把商品库存数量加载到Redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * GET POST有什么区别
     * GET是幂等的，从服务端获取数据，调用多少次结果都一样，不会对服务端数据造成任何影响
     * POST不是幂等的，向服务端提交数据，对服务端数据造成影响
     */
    @RequestMapping(value="/{path}/do_miaosha", method= RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLIEGAL);
        }
        //内存标记，减少redis访问，无库存后的请求无需访问redis
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //收到请求，redis预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否已经秒杀到了，不能重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0); //排队中


        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否已经秒杀到了，不能重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单 秒杀成功后进入订单详情页
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);
        */
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0：排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds=5, maxCount=10, needLogin=true)
    @RequestMapping(value="/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods:goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),10);
            localOverMap.put(goods.getId(),false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }

    //隐藏秒杀接口地址
    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value="/path", method= RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode") int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //限流防刷，查询访问次数，拦截器

//        //限制访问次数
//        String uri = request.getRequestURI();
//        String key = uri+"_"+user.getId();
//        //限定key5s之内只能访问5次
//        Integer count=redisService.get(AccessKey.access, key, Integer.class);
//        if(count==null) {
//            redisService.set(AccessKey.access, key, 1);
//        }else if(count<5) {
//            redisService.incr(AccessKey.access, key);
//        }else {//超过5次
//            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//        }
        //校验验证码
        boolean check = miaoshaService.checkVerifyCode(user, goodsId,verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLIEGAL);
        }
        //返回访问路径
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value="/verifyCode", method= RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, Model model, MiaoshaUser user,
                                               @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG",out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }



}
