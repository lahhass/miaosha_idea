package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.redis.GoodsKey;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.GoodsService;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.vo.GoodsDetailVo;
import com.lahhass.miaosha.vo.GoodsVo;
import com.lahhass.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Administrator on 2019/5/11.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    //有时候手机端不会把token放入cookie，直接作为参数传
    //首先先从param中取token，取不到再从cookie中取

    //页面缓存
    @RequestMapping(value="/to_list",produces="text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model) {
        //取页面缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //model.addAttribute("user", user);

        //查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        //return "goods_list";

        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,"",html);  //缓存有效期1分钟
        }
        return html;
    }





    //url缓存
    @RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response,
                            Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, ""+goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //手动渲染
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //毫秒单位
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) { //秒杀未开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        } else if (now > endAt) { //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";

        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", springWebContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);  //缓存有效期1分钟
        }
        return html;
    }

    //url缓存
    // 静态内容为html 动态数据通过接口获取
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response,
                                          Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //毫秒单位
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) { //秒杀未开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now)/1000);
        } else if (now > endAt) { //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }

}
