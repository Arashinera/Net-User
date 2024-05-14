package app.user;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Locale;
import java.util.Scanner;

public final class NettyUser {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8666;


    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        Scanner input = new Scanner(System.in);
        input.useLocale(Locale.ENGLISH);

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new UserHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect(HOST, PORT).sync();

            System.out.print("Please, input your username : ");
            String userName = input.nextLine();

            Channel channel = f.sync().channel();
            channel.writeAndFlush(userName);
            channel.flush();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
