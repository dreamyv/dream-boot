package com.dream.netty;

import com.dream.config.ProjectProperties;
import com.dream.core.netty.decode.MessageDecode;
import com.dream.core.netty.server.AbstractNettyServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@SuppressWarnings("all")
public class DreamNettyServer extends AbstractNettyServer {

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private ProjectProperties properties;

    /**
     * 系统启动初始化方法,启动netty客户端
     */
    @PostConstruct
    @Override
    public void start() {
        this.setPort(properties.getPort());
        super.start();
    }

    /**
     * 系统关闭,停止netty客户端
     */
    @PreDestroy
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * 定义netty后续每条连接的数据读写,业务处理逻辑
     */
    @Override
    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(
                        //拆包处理器
                        new MessageDecode(properties.getMaxFrameLength(),properties.getLengthFieldOffset(),properties.getLengthFieldLength(),properties.getLengthAdjustment()
                                ,properties.getInitialBytesToStrip(),properties.getFailFast()),
                        //设置心跳时间.一定时间未接收和写入数据后,触发userEventTriggered方法
                        new IdleStateHandler(properties.getTimeout(),properties.getTimeout(), 0),
                        //业务处理逻辑
                        messageHandler
                );
            }
        };
    }

}
