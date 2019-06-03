package com.dream.netty;

import com.dream.core.netty.cache.CommonCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Netty业务拆包处理器
 */
@ChannelHandler.Sharable
@Service
@SuppressWarnings("all")
public class MessageHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * 接收netty数据(拆包后的数据)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg!=null){
            LOGGER.info("receive protrocol {}",msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        LOGGER.debug("client ip [{}] disconnected succeed",channel.remoteAddress().toString());
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
                LOGGER.debug("sn[{}] disconnected clean cache logout succeed",sn);
//            }
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  throws Exception{
        LOGGER.debug("client ip [{}] connected succeed",ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx,cause);
    }

    /**
     * 一定时间内(90S),ChannelRead未被调用,触发此方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            //监听90秒未上报数据的空闲客户端连接,进行清理
            if(state == IdleState.READER_IDLE){
                channel.close();
            }
        }
    }
}
