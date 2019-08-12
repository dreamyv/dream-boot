package com.dream.core.netty.server;

import com.dream.core.netty.IServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty服务端公用方法
 * @author dream.jin
 */
public abstract class AbstractNettyServer implements IServer {

    private static final Logger loggge = LoggerFactory.getLogger(AbstractNettyServer.class);

    /**
     * 表示监听端口，创建新连接的线程组
     */
    private EventLoopGroup bossGroup;
    /**
     * 表示处理每一条连接的数据读写的线程组
     */
    private EventLoopGroup workerGroup;
    /**
     * netty服务端端口
     */
    protected int port;

    /**
     * 启动netty服务端:
     * 1、创建一个引导类
     * 2、必须要指定三类属性，分别是1、线程模型 2、IO模型 3、连接读写处理逻辑
     * 3、绑定端口
     */
    @Override
    public void start()  {
        loggge.info("----------------- Start netty server ... -----------------");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        //引导类,进行服务端的启动工作
        ServerBootstrap bootstrap = new ServerBootstrap();
        //引导类配置两大线程,引导类的线程模型
        bootstrap.group(bossGroup, workerGroup)
                //channel指定IO模型,NioServerSocketChannel:NIO
                .channel(NioServerSocketChannel.class)
                //SO_BACKLOG:系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 100)
                //childHandler定义后续每条连接的数据读写，业务处理逻辑
                .childHandler(getChannelInitializer());
        try {
            //本地绑定一个端口启动(异步方法,可监听..)
            ChannelFuture server = bootstrap.bind(port).sync();
            loggge.info("----------------- Start netty server succeed! port:[{}] -----------------",port);
        } catch (InterruptedException e) {
            throw new RuntimeException("Server netty start error!", e);
        }
    }

    /**
     * 连接neyyt读写处理逻辑
     * 抽象方法,子类实现
     * @return
     */
    public abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    /**
     * 停止netty
     */
    @Override
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void setPort(int port) {
        this.port = port;
    }


}
