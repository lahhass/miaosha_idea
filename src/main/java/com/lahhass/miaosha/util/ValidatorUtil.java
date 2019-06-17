package com.lahhass.miaosha.util;




import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidatorUtil {

    //正则表达式：第一位为1，后面加10位数字
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("18933445678"));
//        System.out.println(isMobile("189334458"));
//    }
}
