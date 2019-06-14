package com.dream.netty;

import com.dream.core.netty.cache.CommonCache;
import com.dream.core.protocol.MessageProtocol;
import com.dream.handler.MessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Netty业务拆包处理器
 */
@ChannelHandler.Sharable
@Service
@SuppressWarnings("all")
public class MessageChanneHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MessageChanneHandler.class);

    @Autowired
    private MessageHandler messageHandler;

    /**
     * 接收netty数据(拆包后的数据)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg!=null){
            if(!(msg instanceof MessageProtocol)) {
                logger.warn("接收的数据不是MessageProtocol类型!");
                return;
            }
            messageHandler.doBusiness((MessageProtocol)msg,ctx.channel());
        }
    }

    /**
     * 失去连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        logger.info("客户端IP[{}]断连!",channel.remoteAddress().toString());
        if(CommonCache.channelSnMap.containsKey(channel)){
            String sn = CommonCache.channelSnMap.get(channel);
            CommonCache.channelSnMap.remove(channel);
            if(CommonCache.snChannelMap.containsKey(sn)){
                CommonCache.snChannelMap.remove(sn);
            }
//            String cacheData = redisHandler.get(sn);
//            if(StringUtils.isNotBlank(cacheData)){
//                JSONObject jsonObject = JSONObject.parseObject(cacheData);
//                jsonObject.put(Constans.LOGIN,Boolean.FALSE);
//                redisHandler.set(sn,JSONObject.toJSONString(jsonObject));
                logger.debug("sn[{}] disconnected clean cache logout succeed",sn);
//            }
        }
        super.channelInactive(ctx);
    }

    /**
     * 有新客户端连接接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)  throws Exception{
        logger.info("client ip [{}] connected succeed",ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    /**
     * 输出异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        logger.error("客户端IP["+channel.remoteAddress().toString()+"] exceptionCaught error",cause);
        super.exceptionCaught(ctx,cause);
    }

    /**
     * 90秒内(时间可设置),ChannelRead未被调用或未发送数据,触发此方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            //监听一定时间未上报数据的空闲客户端连接,进行清理
            if(state == IdleState.READER_IDLE){
                logger.info("client ip [{}],该连接在一定时间内未收到任何消息,强制关闭连接!",ctx.channel().remoteAddress().toString());
                channel.close();
            }
        }
    }
}
