package com.dream.netty;

import com.dream.config.ProjectProperties;
import com.dream.core.netty.client.AbstractNettyClient;
import com.dream.core.netty.decode.MessageDecode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 模拟TCU客户端
 */
@SuppressWarnings("all")
public class TcuClient extends AbstractNettyClient {


    @Autowired
    private ProjectProperties properties;
    @Autowired
    private TcuHandler tcuHandler;

    public TcuClient(String ip, int port,int connectMaxNum) {
        super(ip, port,connectMaxNum);
    }

    @PostConstruct
    @Override
    public void connect() {
        super.connect();
    }

    /**
     * 连接neyyt读写处理逻辑
     */
    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(
                        //设置解码器
                        new MessageDecode(properties.getMaxFrameLength(),properties.getLengthFieldOffset()
                        ,properties.getLengthFieldLength(),properties.getLengthAdjustment()
                        ,properties.getInitialBytesToStrip(),properties.getFailFast()));
                pipeline.addLast(
                        //设置心跳时间
                        new IdleStateHandler(properties.getTimeout(), properties.getTimeout(), 0),
                        //业务处理
                        tcuHandler
                );
            }
        };
    }

}
