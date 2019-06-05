package com.dream.core.netty.decode;

import com.dream.core.protocol.*;
import com.dream.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 协议Decode拆包处理器
 */
@SuppressWarnings("all")
public class MessageDecode extends LengthFieldBasedFrameDecoder {

    private final static Logger logger = LoggerFactory.getLogger(MessageDecode.class);

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
        try{
            MessageProtocol messageProtocol = this.decode(byteBuf);
            return byteBuf;
        }catch (Exception e){
            logger.error("decode",e);
            return null;
        }
    }

    /**
     * 解码方法
     */
    private MessageProtocol decode(ByteBuf byteBuf) {
        MessageProtocol messageProtocol = new MessageProtocol();
        //固定起始字符 占2个字节
        messageProtocol.setHead(ByteUtil.byteToHex(new byte[]{byteBuf.readByte(),byteBuf.readByte()}));
        //协议版本号 占1个字节
        messageProtocol.setVersion(ProtocolVersion.valueOf(byteBuf.readByte()).name());
        //报文唯一标识 32UUID
        byte[] uuidBytes = new byte[32];
        byteBuf.readBytes(uuidBytes);
        messageProtocol.setUuid(new String(uuidBytes));
        //指令类型 占1个字节
        messageProtocol.setCommand(MessageCommand.valueOf(byteBuf.readByte()).name());
        //采集时间 占6个字节
        byte[] timeBytes = new byte[6];
        byteBuf.readBytes(timeBytes);
        messageProtocol.setCollectTime(ByteUtil.gbTimeArrayLong(timeBytes));
        //sn 占16个字节
        byte[] snBytes = new byte[16];
        byteBuf.readBytes(snBytes);
        messageProtocol.setSn(new String(snBytes));
        //加密单元 占1个字节
        messageProtocol.setEncyption(MessageEncryption.valueOf(byteBuf.readByte()).name());
        //数据单元长度 占4个字节
        Long bodySzie = byteBuf.readUnsignedInt();
        messageProtocol.setBodySize(bodySzie);
        //数据体
        if(bodySzie>0){
            byte[] body = new byte[bodySzie.intValue()];
            byteBuf.readBytes(body);
            String hex = ByteUtil.byteToHex(body);
            ProtocolBody protocolBody = new ProtocolBody();
            protocolBody.setHex(hex);
            messageProtocol.setBody(protocolBody);
        }
        //结束符 占1个字节
        messageProtocol.setEnd(ByteUtil.byteToHex(byteBuf.readByte()));
        messageProtocol.setGatewayReceiveTime(System.currentTimeMillis());
        return messageProtocol;
    }

}
