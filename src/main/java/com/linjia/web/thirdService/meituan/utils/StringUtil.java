package com.linjia.web.thirdService.meituan.utils;

/**
 * Created by yangzhiqi on 15/11/10.
 */
public class StringUtil {

    public static boolean isBlank(Object o){
        if (o == null) return true;
        return "".equals(trim(o.toString()));

    }

    public static String trim(String s){
        String result = s.replaceAll(" +", "");
        return result;
    }

    public static void main(String[] args){
        trim("   ");
    }
}
