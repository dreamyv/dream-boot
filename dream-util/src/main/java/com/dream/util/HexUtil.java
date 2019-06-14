package com.dream.util;


public class HexUtil {

    /**
     * 十进制转二进制
     * @param i
     * @return
     */
    public static String convertBinary(Integer i){
        return Integer.toBinaryString(i);
    }

    /**
     * 十进制转八进制
     * @param i
     * @return
     */
    public static String convertOctal(Integer i){
        return Integer.toBinaryString(i);
    }

    /**
     * 十进制转十六进制
     * @param i
     * @return
     */
    public static String convertHex(Integer i){
        return Integer.toHexString(i);
    }

    /**
     * 十六进制转十进制
     * @param hex
     * @return
     */
    public static String convertHex(String hex){
        return Integer.valueOf(hex, 16).toString();
    }


    /**
     * 获取两位16进制
     * @param b
     * @return
     */
    public static String numToHex2(String b) {
        return String.format("%02x",Integer.parseInt(b)) ;//2表示需要两位16进行数
    }

    /**
     * 获取四位16进制
     * @param b
     * @return
     */
    public static String numToHex4(String b) {
        return String.format("%04x",Integer.parseInt(b));
    }

}
