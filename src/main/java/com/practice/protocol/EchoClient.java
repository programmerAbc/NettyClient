package com.practice.protocol;

import com.practice.protocol.nettyhandler.EchoClientHandler;
import com.practice.protocol.nettyhandler.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class EchoClient {
    EventLoopGroup workerGroup;
    Bootstrap bootstrap;
    ChannelFuture channelFuture;

    public void startClient(String ip, int port) {
        workerGroup = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ReadTimeoutHandler(1000) {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    super.write(ctx, msg, promise);
                                    ctx.flush();
                                }
                            });
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            channelFuture = bootstrap.connect(ip, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
