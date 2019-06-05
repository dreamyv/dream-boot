package com.dream.core.protocol;


import com.dream.core.common.excepion.BaseException;

@SuppressWarnings("all")
public enum ProtocolVersion {

    ONE((byte)0x01,"第一版协议");

    private byte id;
    private String desc;

    ProtocolVersion(byte id, String desc){
        this.id = id;
        this.desc = desc;
    }

    public static ProtocolVersion valueOf(byte id){
        switch (id){
            case (byte) 0x01:
                return ONE;
            default:
                throw new BaseException("Unknown ProtocolVersion id : "+id);
        }
    }

    public static ProtocolVersion getVersion(String decs){
        switch (decs){
            case "ONE":
                return ONE;
            default:
                throw new BaseException("Unknown ProtocolVersion decs : "+decs);
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
