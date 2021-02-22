package com.github.dreamroute.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

import static java.util.Locale.ENGLISH;

/**
 * 
 * @author w.dehi
 *
 */
public class StringUtil {

    private StringUtil() {}
    
    private static final String SPLIT = "_";

    /**
     * 驼峰命名转为下划线命名
     */
    public static String humpToUnderline(String param) {
        StringBuilder sb = new StringBuilder(param);
        int temp = 0;
        if (!param.contains(SPLIT)) {
            for (int i = 0; i < param.length(); i++) {
                if (Character.isUpperCase(param.charAt(i))) {
                    sb.insert(i + temp, SPLIT);
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase(ENGLISH);
    }

    /***
     * 下划线命名转为驼峰命名
     */

    public static String underlineToHump(String param) {
        StringBuilder result = new StringBuilder();
        String[] a = param.split("_");
        for (String s : a) {
            if (!param.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 获取4位长度的验证码
     */
    public static String getValiCode4() {
        return StringUtils.rightPad(String.valueOf(new Random().nextInt(9999)), 4, "0");
    }

    /**
     * 获取6位长度的验证码
     */
    public static String getValiCode6() {
        return StringUtils.rightPad(String.valueOf(new Random().nextInt(999999)), 6, "0");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
