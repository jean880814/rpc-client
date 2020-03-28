package service.consumerWithZk;

import service.consumerWithNetty.NettyRpcClientInvocation;

import java.lang.reflect.Proxy;

public class ZkRpcClientProxy {

    public <T> T proxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ZkRpcClientInvocation());
    }
}
