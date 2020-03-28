package service.consumerWithNetty;

import com.jean.model.NettyRpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NettyRpcClientInvocation implements InvocationHandler {
    private final String ip;
    private final int port;

    public NettyRpcClientInvocation(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        NettyRpcRequest rpcRequest = new NettyRpcRequest();
        rpcRequest.setClassname(method.getDeclaringClass().getName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setTypes(method.getParameterTypes());
        rpcRequest.setArgs(args);
        return rpcInvoke(rpcRequest);
    }

    private Object rpcInvoke(NettyRpcRequest rpcRequest) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final ConsumerHandler consumerHandler = new ConsumerHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    //自定义协议编码器
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                    //对象参数类型编码器
                    pipeline.addLast("encoder", new ObjectEncoder());
                    //对象参数类型解码器
                    pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast("handler",consumerHandler);
                }
            });

            ChannelFuture channelFuture = bootstrap.connect("localhost", port).sync();
            channelFuture.channel().writeAndFlush(rpcRequest).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return consumerHandler.getObject();
    }
}
