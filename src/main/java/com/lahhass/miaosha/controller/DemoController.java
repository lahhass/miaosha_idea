package com.lahhass.miaosha.controller;

import com.lahhass.miaosha.domain.User;
import com.lahhass.miaosha.rabbitmq.MQSender;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.redis.UserKey;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.result.Result;
import com.lahhass.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender sender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    Result<String> mq() {
//
//        sender.send("hello, rabbit");
//        return Result.success("hello,lahhass");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    Result<String> mqTopic() {
//
//        sender.sendTopic("hello, rabbit");
//        return Result.success("hello,lahhass");
//    }
//
//    //swagger
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    Result<String> mqFanout() {
//
//        sender.sendFanout("hello, rabbit");
//        return Result.success("hello,lahhass");
//    }
//
//    @RequestMapping("/mq/headers")
//    @ResponseBody
//    Result<String> mqHeaders() {
//
//        sender.sendHeader("hello, rabbit");
//        return Result.success("hello,lahhass");
//    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "hello world";
    }

    //1.rest api json输出 2.页面

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello,lahhass");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","lahhass");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("11111");

        boolean b = redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(b);
    }
}
