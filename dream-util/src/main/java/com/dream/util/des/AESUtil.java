package com.dream.util.des;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 */
@SuppressWarnings("all")
public class AESUtil {

    public static String AES = null;
    public static String CHARSET_CODE = null;
    static {
        AES = "AES";
        CHARSET_CODE = "UTF-8";
    }

    /**
     * 解密
     * @param data
     * @param securityKey
     * @return
     */
    public final static String decrypt(String data,String securityKey) {
        try {
            return new String(decrypt(hex2byte(data.getBytes()), securityKey), CHARSET_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param data
     * @param securityKey
     * @return
     */
    public final static String encrypt(String data,String securityKey) {
        try {
            return byte2hex(encrypt(data.getBytes(CHARSET_CODE), securityKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(byte[] src, String securityKey) throws Exception{
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(securityKey.getBytes(), AES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }

    private static byte[] decrypt(byte[] src, String securityKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(securityKey.getBytes(), AES);
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }

    private static String byte2hex(byte[] b) {
        String hs = "",stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1){
                hs = hs + "0" + stmp;
            } else{
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0){
            throw new IllegalArgumentException("The length is not even");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
