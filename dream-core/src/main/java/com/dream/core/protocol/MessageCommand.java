package com.dream.core.protocol;


import com.dream.core.common.excepion.BaseException;

/**
 * 定义指令类型
 */
@SuppressWarnings("all")
public enum MessageCommand {

    /**
     * Tbox指令UP
     */
    UP_REGISTER((byte)0x01, "Tbox注册请求"),
    UP_LOGIN((byte)0x02, "登录请求"),
    UP_HEARTBEAT((byte)0x03, "心跳请求"),
    UP_QCPASSED((byte)0x04, "TBox状态检查成功请求"),
    UP_GB_LOGIN((byte)0x05, "国标功能的登入"),
    UP_REALTIME_DATA((byte)0x06, "实时信息上报请求"),
    UP_RE_REALTIME_DATA((byte)0x07, "实时信息补发请求"),
    UP_GB_LOGOUT((byte)0x08,"国标功能的登出"),
    UP_LOGOUT((byte)0x09, "登出请求"),
    UP_STATUS((byte)0x0A, "车况上报"),
    UP_LOCATION((byte)0x0B, "位置上报"),
    UP_WARNING((byte)0x0C, "自定义报警上报"),
    UP_FAULT((byte)0x0D, "自定义故障上报"),
    UP_REMOTECTRL((byte)0x0E, "网关下达远控响应"),
    UP_ALARM((byte)0x0F, "自定义告警上报"),
    UP_POI((byte)0x1A, "POI下达响应"),

    /**
     * 网关指令Down
     */
    DOWN_REGISTER((byte)0xFD, "Tbox注册请求响应"),
    DOWN_LOGIN((byte)0xFC, "登录请求响应"),
    DOWN_HEARTBEAT((byte)0xFB, "心跳请求响应"),
    DOWN_QCPASSED((byte)0xFA, "TBox状态检查成功请求响应"),
    DOWN_GB_LOGIN((byte)0xF9, "国标功能的登入响应"),
    DOWN_REALTIME_DATA((byte)0xF8, "实时信息上报请求响应"),
    DOWN_RE_REALTIME_DATA((byte)0xF7, "实时信息补发请求响应"),
    DOWN_GB_LOGOUT((byte)0xF6,"国标功能的登出响应"),
    DOWN_LOGOUT((byte)0xF5, "登出请求响应"),
    DOWN_STATUS((byte)0xF4, "车况上报响应"),
    DOWN_LOCATION((byte)0xF3, "位置上报响应"),
    DOWN_WARNING((byte)0xF2, "自定义报警上报响应"),
    DOWN_FAULT((byte)0xF1, "自定义故障上报响应"),
    DOWN_REMOTECTRL((byte)0xF0, "网关下达远控请求"),
    DOWN_ALARM((byte)0xEF, "自定义告警上报响应"),
    DOWN_POI((byte)0xEE, "POI下达");

    private byte id;
    private String desc;

    MessageCommand(byte id, String desc){
        this.id = id;
        this.desc = desc;
    }

    public static MessageCommand valueOf(byte id){
        switch (id){
            //上行
            case (byte) 0x01:
                return UP_REGISTER;
            case (byte) 0x02:
                return UP_LOGIN;
            case (byte) 0x03:
                return UP_HEARTBEAT;
            case (byte) 0x04:
                return UP_QCPASSED;
            case (byte) 0x05:
                return UP_GB_LOGIN;
            case (byte) 0x06:
                return UP_REALTIME_DATA;
            case (byte) 0x07:
                return UP_RE_REALTIME_DATA;
            case (byte) 0x08:
                return UP_GB_LOGOUT;
            case (byte) 0x09:
                return UP_LOGOUT;
            case (byte) 0x0A:
                return UP_STATUS;
            case (byte) 0x0B:
                return UP_LOCATION;
            case (byte) 0x0C:
                return UP_WARNING;
            case (byte) 0x0D:
                return UP_FAULT;
            case (byte) 0x0E:
                return UP_REMOTECTRL;
            case (byte) 0x0F:
                return UP_ALARM;
            case (byte) 0x1A:
                return UP_POI;
                //下行
            case (byte) 0xFD:
                return DOWN_REGISTER;
            case (byte) 0xFC:
                return DOWN_LOGIN;
            case (byte) 0xFB:
                return DOWN_HEARTBEAT;
            case (byte) 0xFA:
                return DOWN_QCPASSED;
            case (byte) 0xF9:
                return DOWN_GB_LOGIN;
            case (byte) 0xF8:
                return DOWN_REALTIME_DATA;
            case (byte) 0xF7:
                return DOWN_RE_REALTIME_DATA;
            case (byte) 0xF6:
                return DOWN_GB_LOGOUT;
            case (byte) 0xF5:
                return DOWN_LOGOUT;
            case (byte) 0xF4:
                return DOWN_STATUS;
            case (byte) 0xF3:
                return DOWN_LOCATION;
            case (byte) 0xF2:
                return DOWN_WARNING;
            case (byte) 0xF1:
                return DOWN_FAULT;
            case (byte) 0xF0:
                return DOWN_REMOTECTRL;
            case (byte) 0xEF:
                return DOWN_ALARM;
            case (byte) 0xEE:
                return DOWN_POI;
            default:
                throw new BaseException("Unknown ThunderboltCommand id : "+id);
        }
    }

    public static MessageCommand getDownCommand(String desc){
        switch (desc){
            //上行
            case "UP_REGISTER" :
                return DOWN_REGISTER;
            case "UP_LOGIN":
                return DOWN_LOGIN;
            case "UP_HEARTBEAT":
                return DOWN_HEARTBEAT;
            case "UP_QCPASSED":
                return DOWN_QCPASSED;
            case "UP_GB_LOGIN":
                return DOWN_GB_LOGIN;
            case "UP_REALTIME_DATA":
                return DOWN_REALTIME_DATA;
            case "UP_RE_REALTIME_DATA":
                return DOWN_RE_REALTIME_DATA;
            case "UP_GB_LOGOUT":
                return DOWN_GB_LOGOUT;
            case "UP_LOGOUT":
                return DOWN_LOGOUT;
            case "UP_STATUS":
                return DOWN_STATUS;
            case "UP_LOCATION":
                return DOWN_LOCATION;
            case "UP_WARNING":
                return DOWN_WARNING;
            case "UP_FAULT":
                return DOWN_FAULT;
            case "DOWN_REMOTECTRL":
                return UP_REMOTECTRL;
            case "UP_ALARM":
                return DOWN_ALARM;
            case "DOWN_POI":
                return UP_POI;
            default:
                throw new BaseException("Unknown ThunderboltCommand desc : "+desc);
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
