package com.dream.util.des;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Util {

    private  static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    /**
     * 对字符串md5大写加密
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String encrypt(String str) {
          return encrypt(str.getBytes());
    }


    /**
     * 32位小写加密
     * @param bytes
     * @return
     */
    public static String encrypt(byte[] bytes) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toLowerCase();
    }

}
