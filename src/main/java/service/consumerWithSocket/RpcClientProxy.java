package service.consumerWithSocket;

import java.lang.reflect.Proxy;

public class RpcClientProxy {
    private final String ip;
    private final int port;

    public RpcClientProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> T proxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RpcClientInvocation(ip, port));
    }
}
