package com.dream.core.netty.client;

import com.dream.core.netty.IClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * netty客户端抽象公用类
 */
@SuppressWarnings("all")
public abstract class AbstractNettyClient implements IClient {
    private static Logger logger = LoggerFactory.getLogger(AbstractNettyClient.class);

    /**
     * 服务端IP地址
     */
    protected String ip;
    /**
     * 服务端端口
     */
    protected int port;
    /**
     * 是否连接成功
     */
    protected volatile boolean isConnected;
    /**
     * 连接是否关闭
     */
    protected volatile boolean isClosed;
    /**
     * 已连接次数
     */
    protected int connectNum;
    /**
     * 连接最大次数
     */
    protected int connectMaxNum;
    /**
     * 连接建立后返回相应的channel通道对象
     */
    protected Channel channel;
    /**
     * 引导类,进行服务端的启动工作
     */
    protected Bootstrap bootstrap;
    /**
     * 表示每一条连接的数据写的线程组
     */
    private NioEventLoopGroup workerGroup;
    /**
     * 线程池
     */
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    public AbstractNettyClient(String ip, int port,int connectMaxNum) {
        this.ip = ip;
        this.port = port;
        this.connectMaxNum = connectMaxNum;
    }

    public AbstractNettyClient() {
    }

    /**
     * netty连接服务端
     */
    public void connect() {
        connect(null);
    }

    /**
     *  netty连接服务端
     * @param listener
     */
    public void connect(final ClientEventListener listener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                connect(ip, port, listener);
            }
        });
    }

    /**
     *  netty连接服务端
     */
    private void connect(String ip, int port,final ClientEventListener listener) {
        try {
            workerGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    //channel指定IO模型,NioServerSocketChannel:NIO
                    .channel(NioSocketChannel.class)
                    //指定服务端IP和端口
                    .remoteAddress(ip, port)
                    //ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //ChannelOption.TCP_NODELAY表示是否开始Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，
                    // 有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                    //.option(ChannelOption.TCP_NODELAY, true)
                    //定义后续每条连接的数据读写，业务处理逻辑
                    .handler(getChannelInitializer());

            //连接服务端,并添加监听判读是否成功
            channel = bootstrap.connect().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    //连接成功
                    if(future.isSuccess()) {
                        logger.info("---------- Client connect succeed! ----------");
                        isConnected = true;
                        if(listener != null) {
                            listener.onConnected();
                        }
                    }
                }
            }).sync().channel();

            //监听到连接关闭
            channel.closeFuture().addListener(new ChannelFutureListener(){
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    isConnected = false;
                    logger.info("---------- Client connection closed! ---------- ");
                    if(listener != null) {
                        listener.onClosed();
                    }
                }
            }).sync();

        } catch (Exception e) {
            throw new RuntimeException("---------- Client connect error! ----------", e);
        } finally {
            isConnected = false;
            workerGroup.shutdownGracefully();
            reConnect();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if(!isClosed && channel != null && channel.isOpen()) {
            channel.close();
            isClosed = true;
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        if (!isClosed && !isConnected && (connectNum++ < connectMaxNum)) {
            logger.info("---------- Client re Connect num:[{}],connectMaxNum:[{}]---------- " , connectNum,connectMaxNum);
            try {
                Thread.sleep(3000);
                connect();
            } catch (Exception e) {
                logger.error("netty 重连服务端失败!", e);
            }
        }
    }

    /**
     * 连接neyyt读写处理逻辑
     * 抽象方法,子类实现
     */
    public abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    /**
     * 发送数据
     */
    public void send(final byte[] payload) {
        //if(!isConnected) {
        //}
        executor.execute(new Runnable() {
             @Override
             public void run() {
                 channel.writeAndFlush(payload);
             }
        });
    }

    public void start() {
        connect();
    }

    public void stop() {
        workerGroup.shutdownGracefully();
    }

    public boolean isClose() {
        return isClosed;
    }

    public boolean isConnect() {
        return isConnected;
    }

    public void setClosed(boolean closed) {
        this.isClosed = closed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectMaxNum() {
        return connectMaxNum;
    }

    public void setConnectMaxNum(int connectMaxNum) {
        this.connectMaxNum = connectMaxNum;
    }
}
