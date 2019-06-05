package com.dream.handler;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {

    /**
     * 协议上行业务处理方法,并给出响应指令
     */
    public void doBusiness(byte[] bytes, Channel channel) {

    }
}
