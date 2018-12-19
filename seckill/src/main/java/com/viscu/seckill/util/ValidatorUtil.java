package com.viscu.seckill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 手机号码验证
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(mobile);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("13421474495"));
        System.out.println(isMobile("1342147449"));
    }

}
