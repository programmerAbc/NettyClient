package com.practice.protocol.nettyhandler;

import com.practice.protocol.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class EchoClientHandler extends ByteToMessageDecoder {
    long timestamp = 0;
    Long latency = null;
    long startTime = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        timestamp = System.currentTimeMillis();
        System.out.println("channelActive at" + timestamp);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (latency == null) {
            startTime = System.currentTimeMillis();
            latency = System.currentTimeMillis() - timestamp;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
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
            if (frameBytes[8] == ProtocolUtil.CONTENT_TYPE_TIME[0] && frameBytes[9] == ProtocolUtil.CONTENT_TYPE_TIME[1]) {
                byte[] timeBytes = new byte[8];
                System.arraycopy(frameBytes, ProtocolUtil.HEADER_LENGTH, timeBytes, 0, timeBytes.length);
                long time = ProtocolUtil.convertBytesToLong(timeBytes) + latency;
                long offset = time - startTime;
                System.out.println("offset:" + offset);
            }
        }
    }
}
