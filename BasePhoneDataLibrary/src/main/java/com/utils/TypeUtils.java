package com.utils;

/**
 * Created by：bobby on 2021-10-25 19:10.
 * Describe：
 */
public class TypeUtils {
    public static int type;

    public TypeUtils() {
    }

    public static void setType(int type) {
        TypeUtils.type = type;
    }

    public static int getType() {
        return type;
    }

    public static String getSMSData() {
        int type = getType();
        String s1 = type == 1 ? "conten" : "";
        String s2 = type == 1 ? "t:" : "";
        String s3 = type == 1 ? "//sms/" : "";
        return s1 + s2 + s3;
    }

    public static String getContentPhoneLookup() {
        int type = getType();
        String s1 = type == 1 ? "cont" : "";
        String s2 = type == 1 ? "ent://" : "";
        String s3 = type == 1 ? "com.andro" : "";
        String s4 = type == 1 ? "id.contacts/" : "";
        return s1 + s2 + s3 + s4;
    }

    public static String getContentData() {
        int type = getType();
        String s1 = type == 1 ? "con" : "";
        String s2 = type == 1 ? "tent://c" : "";
        String s3 = type == 1 ? "om.androi" : "";
        String s4 = type == 1 ? "d.contacts/d" : "";
        String s5 = type == 1 ? "ata/phones" : "";
        return s1 + s2 + s3 + s4 + s5;
    }
} 