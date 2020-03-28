package service.consumerWithNetty;

import java.lang.reflect.Proxy;

public class NettyRpcClientProxy {
    private final String ip;
    private final int port;

    public NettyRpcClientProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> T proxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new NettyRpcClientInvocation(ip, port));
    }
}
