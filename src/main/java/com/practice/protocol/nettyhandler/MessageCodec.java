package com.practice.protocol.nettyhandler;

import com.alibaba.fastjson.JSON;
import com.practice.protocol.model.Message;
import com.practice.protocol.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MessageCodec extends ByteToMessageCodec<Message> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeBytes(ProtocolUtil.makeJsonFrame(JSON.toJSONString(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (true) {
            if (in.readableBytes() < ProtocolUtil.HEADER_LENGTH) return;
            int start = in.readerIndex();
            int upLimit = start + ProtocolUtil.FRAME_LENGTH_BLOCK;
            byte[] frameLengthBytes = new byte[ProtocolUtil.FRAME_LENGTH_BLOCK];
            for (int i = start; i < upLimit; ++i) {
                try {
                    frameLengthBytes[i - start] = in.getByte(i);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            int frameLength = (int) ProtocolUtil.convertBytesToLong(frameLengthBytes);
            if (in.readableBytes() < frameLength) return;
            byte[] frameBytes = new byte[frameLength];
            in.readBytes(frameBytes);
            if (frameBytes[8] == ProtocolUtil.CONTENT_TYPE_JSON[0] && frameBytes[9] == ProtocolUtil.CONTENT_TYPE_JSON[1]) {
                String json = new String(frameBytes, ProtocolUtil.HEADER_LENGTH, frameLength - ProtocolUtil.HEADER_LENGTH, CharsetUtil.UTF_8);
                Message message = JSON.parseObject(json, Message.class);
                out.add(message);
            }
        }
    }
}
