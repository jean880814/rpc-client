import com.jean.service.UserService;
import service.RpcClientProxy;

public class App {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("localhost", 8080);
        UserService userService = rpcClientProxy.proxy(UserService.class);
        System.out.println(userService.getUser("jean"));
    }
}
