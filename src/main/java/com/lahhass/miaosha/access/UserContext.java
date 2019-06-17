package com.lahhass.miaosha.access;

import com.lahhass.miaosha.domain.MiaoshaUser;


public class UserContext {
    //ThreadLocal与当前线程绑定，多线程下保证线程安全
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}
