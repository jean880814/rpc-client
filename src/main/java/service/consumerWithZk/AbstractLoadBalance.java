package service.consumerWithZk;

import java.util.List;

public abstract class AbstractLoadBalance implements ILoadBalance {
    @Override
    public String loadBalance(List<String> repos) {
        if (repos == null || repos.isEmpty()) {
            return null;
        }
        if (repos.size() == 1) {
            return repos.get(0);
        }
        return doBalance(repos);
    }

    public abstract String doBalance(List<String> repos);
}
