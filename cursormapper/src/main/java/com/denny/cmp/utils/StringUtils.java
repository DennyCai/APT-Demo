package com.denny.cmp.utils;

/**
 * Created by hasee on 2016/4/27.
 */
public final class StringUtils {

    public static String toUpperCaseAt(String str,int index){
        if(null==str||"".equals(str)||index<0){
            return null;
        }
        if(index>=str.length())
            return null;
        char[] arr = new char[str.length()];
        str.getChars(0,str.length(),arr,0);
        arr[index] = Character.toUpperCase(arr[index]);
        return new String(arr);
    }
}
