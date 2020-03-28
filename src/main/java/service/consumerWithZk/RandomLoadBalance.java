package service.consumerWithZk;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    public String doBalance(List<String> repos) {
        Random random = new Random();
        return repos.get(random.nextInt(repos.size()));
    }
}
