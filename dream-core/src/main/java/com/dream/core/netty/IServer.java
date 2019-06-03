package com.dream.core.netty;

/**
 * 定义服务端公共接口
 */
@SuppressWarnings("all")
public interface IServer {
    /**
     * 启动
     */
    public void start();

    /**
     * 停止
     */
    public void stop();
}