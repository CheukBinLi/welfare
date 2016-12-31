package com.welfare.common.util;

import java.util.Random;

public class StringUtil {

    /**
     * 随机生成由大小写与数字组成的指定长度的字符串
     */
    public static String randomString(int length) {
        Random rand = new Random();
        StringBuffer result = new StringBuffer();
        char[] letters = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < length; i++) {
            result.append(letters[rand.nextInt(61)]);
        }
        return result.toString();
    }
}
