package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.GoodsService;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;


    //有时候手机端不会把token放入cookie，直接作为参数传
    //首先先从param中取token，取不到再从cookie中取

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }


}
