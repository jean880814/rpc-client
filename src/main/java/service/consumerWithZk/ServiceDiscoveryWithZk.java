package service.consumerWithZk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoveryWithZk implements IDiscovery {
    private static String CONNECTION_STR = "192.168.126.128:2181,192.168.126.129:2181,192.168.126.130:2181";
    private static CuratorFramework curatorFramework;
    private List<String> serviceRepos = new ArrayList<String>();

    static {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_STR).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).namespace("registry").build();
        curatorFramework.start();
    }

    public String discovery(String serviceName) {
        String servicePath = "/" + serviceName;
        if (serviceRepos.isEmpty()) {
            try {
                serviceRepos = curatorFramework.getChildren().forPath(servicePath);
                watch(servicePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ILoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.loadBalance(serviceRepos);
    }

    private void watch(String servicePath) throws Exception {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            System.out.println("服务发送变化" + pathChildrenCacheEvent.getData());
            serviceRepos = curatorFramework.getChildren().forPath(servicePath);
        };
        nodeCache.getListenable().addListener(pathChildrenCacheListener);
        nodeCache.start();
    }
}
