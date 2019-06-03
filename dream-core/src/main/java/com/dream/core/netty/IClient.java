package com.dream.core.netty;

/**
 * 定义客户端公共接口
 */
@SuppressWarnings("all")
public interface IClient {
    /**
     * 启动
     */
    public void start();

    /**
     * 停止
     */
    public void stop();
}
