package com.dream.handler;

import com.alibaba.fastjson.JSONObject;
import com.dream.core.protocol.MessageProtocol;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {

    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * 协议上行业务处理方法,并给出响应指令
     */
    public void doBusiness(MessageProtocol messageProtocol, Channel channel) {
        logger.info("receive:[{}]", JSONObject.toJSONString(messageProtocol));
    }
}
