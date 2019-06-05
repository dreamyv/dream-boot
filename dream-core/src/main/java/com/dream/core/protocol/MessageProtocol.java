package com.dream.core.protocol;

import java.io.Serializable;
import java.util.List;

/**
 * 数据自定义协议
 */
@SuppressWarnings("all")
public class MessageProtocol implements Serializable{

    /**
     * 起始符号## 占两个字节 0x23 0x23
     */
    private String head;
    /**
     * 协议版本号  占用1个字节
     */
    private String version;
    /**
     * 报文唯一32位UUID 占32个字节
     */
    private String uuid;
    /**
     * 指令类型  占用1个字节
     */
    private String command;
    /**
     * 采集时间 秒级的时间戳 占用6个字节 国家规定年月日时分秒
     */
    private Long collectTime;
    /**
     * Tbox序列号占用16个字节 行业标准 空格+16位
     */
    private String sn;
    /**
     * 加密方式  占用1个字节
     */
    private String encyption;
    /**
     * 报文体长度  占用4个字节
     */
    private Long bodySize;
    /**
     * 报文体
     */
    private ProtocolBody body;
    /**
     * 最后1位 以$结束 0x24
     */
    private String end;
    /**
     * 网关发送dispatcher使用该字段 协议内部不使用
     */
    private String vin;
    /**
     * 网关发送dispatcher使用该字段 协议内部不使用
     */
    private List<String> uploadTopics;
    /**
     * 网关发送dispatcher 网关接收时间 协议内部不使用
     */
    private Long gatewayReceiveTime;
    /**
     * 网关发送dispatcher转发时间 协议内部不使用
     */
    private Long gatewayForwardTime;
    /**
     * dispatcher接收时间 协议内部不使用
     */
    private Long dispatcherReceiveTime;
    /**
     * uplpader接收时间 协议内部不使用
     */
    private Long uploaderReceiveTime;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getEncyption() {
        return encyption;
    }

    public void setEncyption(String encyption) {
        this.encyption = encyption;
    }

    public Long getBodySize() {
        return bodySize;
    }

    public void setBodySize(Long bodySize) {
        this.bodySize = bodySize;
    }

    public ProtocolBody getBody() {
        return body;
    }

    public void setBody(ProtocolBody body) {
        this.body = body;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<String> getUploadTopics() {
        return uploadTopics;
    }

    public void setUploadTopics(List<String> uploadTopics) {
        this.uploadTopics = uploadTopics;
    }

    public Long getGatewayReceiveTime() {
        return gatewayReceiveTime;
    }

    public void setGatewayReceiveTime(Long gatewayReceiveTime) {
        this.gatewayReceiveTime = gatewayReceiveTime;
    }

    public Long getGatewayForwardTime() {
        return gatewayForwardTime;
    }

    public void setGatewayForwardTime(Long gatewayForwardTime) {
        this.gatewayForwardTime = gatewayForwardTime;
    }

    public Long getDispatcherReceiveTime() {
        return dispatcherReceiveTime;
    }

    public void setDispatcherReceiveTime(Long dispatcherReceiveTime) {
        this.dispatcherReceiveTime = dispatcherReceiveTime;
    }

    public Long getUploaderReceiveTime() {
        return uploaderReceiveTime;
    }

    public void setUploaderReceiveTime(Long uploaderReceiveTime) {
        this.uploaderReceiveTime = uploaderReceiveTime;
    }
}
