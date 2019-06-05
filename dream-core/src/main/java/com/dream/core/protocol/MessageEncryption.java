package com.dream.core.protocol;


import com.dream.core.common.excepion.BaseException;

/**
 * 数据加密类型
 */
@SuppressWarnings("all")
public enum MessageEncryption {

    NONE((byte)0x01,"不加密"),
    RSA((byte)0x02,"RSA算法"),
    AES((byte)0x03,"AES128位算法"),
    ERROR((byte)0xFE,"表示异常"),
    INVALID((byte)0xFF,"无效");

    private byte id;
    private String desc;

    MessageEncryption(byte id, String desc){
        this.id = id;
        this.desc = desc;
    }

    public static MessageEncryption valueOf(byte id){
        switch (id){
            case 0x01:
                return NONE;
            case 0x02:
                return RSA;
            case 0x03:
                return AES;
            case (byte)0xFE:
                return ERROR;
            case (byte) 0xFF:
                return INVALID;
            default:
                throw new BaseException("Unknown fvGBEncryptionModel id : "+id);
        }
    }

    public static MessageEncryption getEncryption(String desc){
        switch (desc){
            case "NONE":
                return NONE;
            case "RSA":
                return RSA;
            case "AES":
                return AES;
            case "ERROR":
                return ERROR;
            case "INVALID":
                return INVALID;
            default:
                throw new BaseException("Unknown fvGBEncryptionModel desc : "+desc);
        }
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
