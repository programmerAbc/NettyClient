package com.practice.protocol.nettyhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;

import java.net.InetSocketAddress;
import java.util.List;

public class DatagramPacketCodec extends MessageToMessageCodec<DatagramPacket, ByteBuf> {
    InetSocketAddress broadcardAddress = new InetSocketAddress("255.255.255.255", 65531);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(new DatagramPacket(msg, broadcardAddress));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        out.add(Unpooled.copiedBuffer(msg.content()));
    }
}
