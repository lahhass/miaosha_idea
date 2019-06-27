package com.lahhass.miaosha.config;

import com.lahhass.miaosha.access.UserContext;
import com.lahhass.miaosha.domain.MiaoshaUser;
import com.lahhass.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == MiaoshaUser.class; //类型是MiaoshaUser才做处理
    }

    public Object resolveArgument(MethodParameter methodParameter,
           ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
           WebDataBinderFactory webDataBinderFactory) throws Exception {
        //HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        //HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
            return UserContext.getUser();
    }


}
