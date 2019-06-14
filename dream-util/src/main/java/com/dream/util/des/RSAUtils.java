package com.dream.util.des;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类
 */
public class RSAUtils {
    private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    public static final String RSA = "RSA";//加密方式
    public static final int keyLength = 1024;//加密长度

    //生成密钥对
    public static KeyPair genKeyPair(int keyLength){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSAUtils.RSA);
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //公钥加密
    public static byte[] encrypt(byte[] content, PublicKey publicKey) {
        try{
            Cipher cipher = Cipher.getInstance(RSAUtils.RSA);//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //私钥解密
    public static byte[] decrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSAUtils.RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("私钥解密失败!");
            return null;
        }
    }

    /**
     * 通过字符串生成公钥
     */
    public static PublicKey getPublicKey(String publicKeyData) {
        PublicKey publicKey = null;
        try {
            byte[] decodeKey = decryptBase64(publicKeyData);//Base64解码
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSAUtils.RSA);
            publicKey = keyFactory.generatePublic(x509);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 通过字符串生成私钥
     */
    public static PrivateKey getPrivateKey(String privateKeyData){
        PrivateKey privateKey = null;
        try {
            byte[] decodeKey = decryptBase64(privateKeyData); //将字符串Base64解码
            PKCS8EncodedKeySpec x509 = new PKCS8EncodedKeySpec(decodeKey);//创建x509证书封装类
            KeyFactory keyFactory = KeyFactory.getInstance(RSAUtils.RSA);//指定RSA
            privateKey = keyFactory.generatePrivate(x509);//生成私钥
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * Base64转码
     * @param src
     * @return
     */
    public static String encryptBase64(byte[] src){
        String basicEncoded = Base64.encode(src);
        return basicEncoded;
    }

    /**
     * Base64解码
     * @param str
     * @return
     */
    public static byte[] decryptBase64(String str){
        byte[] basicEncoded = Base64.decode(str);
        return basicEncoded;
    }

}
