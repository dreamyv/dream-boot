package com.dream.core.protocol;

/**
 * 协议报文体
 */
@SuppressWarnings("all")
public class ProtocolBody {

    private String json;

    private String hex;

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
