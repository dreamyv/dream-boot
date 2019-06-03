package com.dream.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * 在连接成功时赋值
 */
@SuppressWarnings("all")
public enum TcuChannel {

    INSTANCE;

    private ChannelHandlerContext channelHandlerContext;

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
