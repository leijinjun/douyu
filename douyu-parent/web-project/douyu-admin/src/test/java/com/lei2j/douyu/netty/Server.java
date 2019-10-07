package com.lei2j.douyu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by lei2j on 2018/7/12.
 */
public class Server {

    public static void main(String[] args)throws Exception {
        EventLoopGroup g = new NioEventLoopGroup();
        EventLoopGroup w = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(g,w);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addFirst("encoder",new StringEncoder());
                pipeline.addLast("handler", new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                        System.out.println("接受client:"+s);
                        channelHandlerContext.writeAndFlush("send serialization \n");
                    }
                });
            }
        });
        bootstrap.bind(9000).sync();
    }

    public void connect() throws Exception{
        java.nio.channels.ServerSocketChannel socketChannel = java.nio.channels.ServerSocketChannel.open();
    }
}
