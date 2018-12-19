package com.viscu.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 描述
 */
public class MD5Util {

    public static String md5(String password){
        return DigestUtils.md5Hex(password);
    }

    private static final String salt = "1a2b3c4d";


    public static String inputPassFormPass(String inputPass){
        System.out.println(salt.charAt(0));
        String newPassword = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        System.out.println(newPassword);//12123456c3
        return md5(newPassword);
    }

    public static String formPassDBPass(String formPass, String salt){
        String newPassword = salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(newPassword);
    }

    public static String inputPassToDBPass(String input, String saltDB){
        String formPass = inputPassFormPass(input);
        String dbPass = formPassDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassFormPass("123456"));//12123456c3
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }

}
