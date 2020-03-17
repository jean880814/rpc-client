package service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConsumerHandler extends ChannelInboundHandlerAdapter {
    private Object object;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        object = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public Object getObject() {
        return this.object;
    }
}
