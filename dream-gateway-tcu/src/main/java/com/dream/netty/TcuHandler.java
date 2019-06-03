package com.dream.netty;

import com.dream.util.ByteUtil;
import com.dream.util.Constans;
import com.dream.util.UUIDUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteOrder;

@ChannelHandler.Sharable
@Service
@SuppressWarnings("all")
public class TcuHandler extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TcuHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg != null){
            LOGGER.info("DOWN_FAULT:{}",msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("server ip [{}] disconnected",ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TcuChannel.INSTANCE.setChannelHandlerContext(ctx);
        LOGGER.debug("server ip[{}] connected succeed",ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if(state == IdleState.WRITER_IDLE){
                ByteBuf heartBuf = this.getHeartBeat();
                ctx.channel().writeAndFlush(heartBuf);
                heartBuf=null;
                LOGGER.info("send hearbeat end");
            }
        }
    }

    /**
     * 获取模拟心跳包
     * @return
     */
    private ByteBuf getHeartBeat() {
        byte[] commonHead = new byte[24];
        commonHead[0]=(byte)0x23;
        commonHead[1]=(byte)0x23;
        commonHead[2]=(byte)0x07;//心跳指令
        commonHead[3]=(byte)0xFE;
        byte[] vinBytes = "ABCDE600000000009".getBytes();
        for (int i = 0; i < vinBytes.length ; i++) {
            commonHead[i+4] = vinBytes[i];
        }
        commonHead[21]= (byte)0x01;
        byte[] bytes = ByteUtil.shortToBytes((short) 0);
        commonHead[22]=bytes[0];
        commonHead[23]=bytes[1];
        byte[] heartBytes = addCommonTail(commonHead);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.order(ByteOrder.BIG_ENDIAN);
        byteBuf.writeBytes(heartBytes);
        LOGGER.info(ByteUtil.bytesToHexString(ByteUtil.decodeValue(byteBuf.nioBuffer())));
        return byteBuf;
    }

    /**
     * 从命令单元起进行BCC计算，获取最后一位校验和填充
     * 追加固定的BCC报文尾
     */
    private static byte[] addCommonTail(byte[] bytes){
        //去除##进行异或校验和
        byte[] bccBytes = new byte[bytes.length-2];
        for (int i = 0; i < bccBytes.length ; i++) {
            bccBytes[i] = bytes[i+2];
        }
        byte bcc = ByteUtil.getBCC(bccBytes);
        //组装校验和
        byte[] encode = new byte[bytes.length+1];
        for (int i = 0; i < bccBytes.length ; i++) {
            encode[i] = bytes[i];
        }
        encode[encode.length-1]=bcc;
        return encode;
    }

}
