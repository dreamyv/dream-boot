package com.dream.core.netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 协议Decode拆包处理器
 */
@SuppressWarnings("all")
public class MessageDecode extends LengthFieldBasedFrameDecoder {

    /**
     * 设置协议
     * @param maxFrameLength 单个包最大的长度，这个值根据实际场景而定
     * @param lengthFieldOffset 数据长度字段开始的偏移量(下标)
     * @param lengthFieldLength 数据长度字段的所占的字节数
     * @param lengthAdjustment 数据(body)长度字段之后剩下包的字节数
     * @param initialBytesToStrip 表示从整个包第一个字节开始，向后忽略的字节数
     * @param failFast 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异
     */
    public MessageDecode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip,failFast);
    }

    /**
     * 拆包的decode方法
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        //在这里调用父类的方法,实现指得到想要的部分,这里全部都要,也可以只要body部分
        byteBuf = (ByteBuf) super.decode(ctx,byteBuf);
        return byteBuf;
    }

}
