package com.lahhass.miaosha.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target(ElementType.METHOD) //定义注解的作用目标为方法
public @interface AccessLimit {
    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
