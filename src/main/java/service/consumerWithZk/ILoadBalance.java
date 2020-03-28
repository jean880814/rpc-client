package service.consumerWithZk;

import java.util.List;

public interface ILoadBalance {
    String loadBalance(List<String> repos);
}
