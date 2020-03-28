import com.jean.model.User;
import com.jean.service.HelloService;
import com.jean.service.UserService;
import service.consumerWithNetty.NettyRpcClientProxy;
import service.consumerWithZk.ZkRpcClientProxy;

public class App {
    public static void main(String[] args) {
//        RpcClientProxy rpcClientProxy = new RpcClientProxy("localhost", 8080);
//        UserService userService = rpcClientProxy.proxy(UserService.class);
//        System.out.println(userService.getUser("jean"));

//        NettyRpcClientProxy rpcClientProxy = new NettyRpcClientProxy("localhost", 2181);
//        UserService userService = rpcClientProxy.proxy(UserService.class);
//        System.out.println(userService.getUser("jean"));
//        User user = new User();
//        user.setName("jean");
//        user.setAge(18);
//        userService.saveUser(user);
//        HelloService helloService = rpcClientProxy.proxy(HelloService.class);
//        System.out.println(helloService.say("jean"));

        UserService userService = new ZkRpcClientProxy().proxy(UserService.class);
        System.out.println(userService.getUser("jean"));
        User user = new User();
        user.setName("jean");
        user.setAge(18);
        userService.saveUser(user);
        HelloService helloService = new ZkRpcClientProxy().proxy(HelloService.class);
        System.out.println(helloService.say("jean"));
    }
}
