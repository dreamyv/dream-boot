package com.dream.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ProjectProperties.PREFIX)
public class ProjectProperties {
    //引用前缀
    public static final String PREFIX = "project";
    //netty-超时时间-心跳
    private Integer timeout;
    //netty-单个包最大的长度，这个值根据实际场景而定
    private Integer maxFrameLength;
    //netty-数据长度字段开始的偏移量(下标)
    private Integer lengthFieldOffset;
    //netty-数据长度字段的所占的字节数
    private Integer lengthFieldLength;
    //netty-（lengthAdjustment + 数据长度取值 = 数据(body)长度字段之后剩下包的字节数）
    private Integer lengthAdjustment;
    //netty-表示从整个包第一个字节开始，向后忽略的字节数
    private Integer initialBytesToStrip;
    //netty-为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异
    private Boolean failFast;
    //netty-服务端IP
    private String serverIp;
    //netty-服务端端口
    private Integer serverPort;
    //netty-重连次数
    private Integer reconnectMaxNum;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxFrameLength() {
        return maxFrameLength;
    }

    public void setMaxFrameLength(Integer maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    public Integer getLengthFieldOffset() {
        return lengthFieldOffset;
    }

    public void setLengthFieldOffset(Integer lengthFieldOffset) {
        this.lengthFieldOffset = lengthFieldOffset;
    }

    public Integer getLengthFieldLength() {
        return lengthFieldLength;
    }

    public void setLengthFieldLength(Integer lengthFieldLength) {
        this.lengthFieldLength = lengthFieldLength;
    }

    public Integer getLengthAdjustment() {
        return lengthAdjustment;
    }

    public void setLengthAdjustment(Integer lengthAdjustment) {
        this.lengthAdjustment = lengthAdjustment;
    }

    public Integer getInitialBytesToStrip() {
        return initialBytesToStrip;
    }

    public void setInitialBytesToStrip(Integer initialBytesToStrip) {
        this.initialBytesToStrip = initialBytesToStrip;
    }

    public Boolean getFailFast() {
        return failFast;
    }

    public void setFailFast(Boolean failFast) {
        this.failFast = failFast;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getReconnectMaxNum() {
        return reconnectMaxNum;
    }

    public void setReconnectMaxNum(Integer reconnectMaxNum) {
        this.reconnectMaxNum = reconnectMaxNum;
    }
}
