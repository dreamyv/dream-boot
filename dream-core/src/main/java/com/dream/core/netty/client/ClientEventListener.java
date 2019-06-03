package com.dream.core.netty.client;

@SuppressWarnings("all")
public interface ClientEventListener {
    /**
     * 连接创建
     */
    public void onConnected();

    /**
     * 连接关闭
     */
    public void onClosed();
}
