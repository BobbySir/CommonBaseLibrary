package com.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by：bobby on 2021-08-19 17:16.
 * Describe：
 */
public class StringUtils {

    public static boolean isEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    public static String isEmptyString(String str){
        return TextUtils.isEmpty(str) ? "" : str;
    }

    //是否是短整型字符串
    public static int isIntString(String str){
        if(isNumber(str)){
            return Integer.parseInt(str);
        }
        return 0;
    }

    //是否是长整型字符串
    public static long isLongString(String str){
        if(isNumber(str)){
            return Long.parseLong(str);
        }
        return 0;
    }

    //是否是整型浮点型数据
    public static double isDoubleString(String str){
        if(isFloat(str)){
            return Double.parseDouble(str);
        }
        return 0;
    }

    //是否是整型浮点型数据
    public static float isFloatString(String str){
        if(isFloat(str)){
            return Float.parseFloat(str);
        }
        return 0;
    }

    // 判断字符串是否是纯数字（不包含浮点型）
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
//		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //判断字符串是否是数字字符串（包括浮点数和整数，正负）
    public static boolean isFloat(String str) {
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数

        boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(str).find();
        boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).find();

        return isInt || isDouble;
    }
} 