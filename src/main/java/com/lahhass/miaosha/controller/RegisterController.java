package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.MiaoshaUserService;
import com.lahhass.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    MiaoshaUserService userService;

    private static Logger log = LoggerFactory.getLogger(RegisterController.class);

    //1.rest api json输出 2.页面

    @RequestMapping("/to_register")
    public String toRegister() {

        return "register";
    }

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<Boolean> doRegister(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        userService.register(response, loginVo);
        System.out.println("register");
        return Result.success(true);
    }

}
