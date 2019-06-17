package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.domain.User;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.service.UserService;
import com.lahhass.miaosha.util.ValidatorUtil;
import com.lahhass.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    MiaoshaUserService userService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    //1.rest api json输出 2.页面

    @RequestMapping("/to_login")
    public String toLogin() {

        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        userService.login(response, loginVo);
        return Result.success(true);
    }

}
