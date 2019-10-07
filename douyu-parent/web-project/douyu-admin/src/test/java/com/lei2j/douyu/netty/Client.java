package com.lei2j.douyu.netty;

import com.lei2j.douyu.admin.danmu.protocol.DouyuMessageProtocol;
import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by lei2j on 2018/7/15.
 */
public class Client {

    public static void main(String[] args) {

        //worker负责读写数据
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //辅助启动类
            Bootstrap bootstrap = new Bootstrap();

            //设置线程池
            bootstrap.group(worker);

            //设置socket工厂
            bootstrap.channel(NioSocketChannel.class);

            //设置管道
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //字符串解码器
//                    pipeline.addLast(new StringDecoder());
                    //字符串编码器
//                    pipeline.addLast(new StringEncoder());
                    //处理类
                    pipeline.addLast(new ClientHandler4());
                }
            });

            //发起异步连接操作
            ChannelFuture futrue = bootstrap.connect(new InetSocketAddress("127.0.0.1",9000)).sync();
            //等待客户端链路关闭
            futrue.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的退出，释放NIO线程组
            worker.shutdownGracefully();
        }
    }

}

class ClientHandler4 extends SimpleChannelInboundHandler<String> {

    //接受服务端发来的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("server response ： "+msg);
    }

    //与服务器建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        //给服务器发消息
        Channel channel = ctx.channel();
        DouyuMessage douyuMessage = new DouyuMessage();
        douyuMessage.add("type", MessageType.LOGIN).add("roomid", "485503");
        channel.writeAndFlush(new String(DouyuMessageProtocol.encode(douyuMessage)));
        DouyuMessage douyuJoin = new DouyuMessage();
        douyuJoin.add("type",MessageType.JOIN_GROUP).add("rid","485503").add("gid","-9999");
        channel.writeAndFlush(new String(DouyuMessageProtocol.encode(douyuJoin)));
    }

    //与服务器断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive");
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //关闭管道
        ctx.channel().close();
        //打印异常信息
        cause.printStackTrace();
    }

}
