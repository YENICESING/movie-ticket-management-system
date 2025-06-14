package com.rabbiter.cm.common.utils;

import java.util.Random;
////////////SaltUtils 类提供了一个简单的方法来生成随机盐，可用于密码加密等场景。
// 通过使用 StringBuilder 存储生成的随机字符，根据指定长度生成随机盐
/**
 * 生成随机盐工具类
 */
public class SaltUtils {

    public static String getSalt(int n) {
        //返回长度为n的随机盐
        StringBuilder sb = new StringBuilder();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getSalt(8));
    }

}
