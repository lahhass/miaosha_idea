package com.lahhass.miaosha.service;

import com.lahhass.miaosha.dao.MiaoshaUserDao;
import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.exception.GlobalException;
import com.lahhass.miaosha.redis.MiaoshaUserKey;
import com.lahhass.miaosha.redis.RedisService;
import com.lahhass.miaosha.result.CodeMsg;
import com.lahhass.miaosha.util.MD5Util;
import com.lahhass.miaosha.util.UUIDUtil;
import com.lahhass.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    private static String salt = "1a2b3c4d";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    //对象缓存
    public MiaoshaUser getById(long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            //加入缓存
            redisService.set(MiaoshaUserKey.getById,""+id,user);
        }
        return user;
    }

    public boolean updatePw(String token, long id, String formPass) {
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    public void register(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user != null) {
            throw new GlobalException(CodeMsg.MOBILE_EXIST);
        }
        //注册
        String calcPass = MD5Util.formPassToDBPass(formPass, salt);
        MiaoshaUser newUser = new MiaoshaUser();
        System.out.println(mobile);
        System.out.println(Long.parseLong(mobile));
        newUser.setId(Long.parseLong(mobile));
        newUser.setPassword(calcPass);
        newUser.setSalt(salt);
        newUser.setRegisterDate(new Date());
        miaoshaUserDao.createAccount(newUser);
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成token,Cookie,将token和对应用户写入第三方缓存redis
        String token = UUIDUtil.uuid();
        addCookie(user, response, token);
        return token;
    }

    private void addCookie(MiaoshaUser user, HttpServletResponse response, String token) {
        //将token和对应用户写入第三方缓存redis
        redisService.set(MiaoshaUserKey.token,token,user);
        //生成Cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //session有效期为最后一次访问时间加有效期时长
    public MiaoshaUser getByToken(String token, HttpServletResponse response) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //生成新cookie,来延长有效期
        if (user != null) {
            addCookie(user, response, token);
        }
        return user;
    }


}
