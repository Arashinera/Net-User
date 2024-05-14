package app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final List<Channel> channels = new ArrayList<>();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("User joined - " + ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String userName) {
        System.out.println("Message received: " + userName);
        for (Channel c : channels) {
            c.writeAndFlush("Hello, " + userName + "! Welcome to the server!" + '\n');
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for user - " + ctx);
        ctx.close();
    }
}
