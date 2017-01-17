package suning;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenwei on 2017/1/16.
 */
public class WorkThreadPool {

    private static ExecutorService workThreadPool = Executors.newFixedThreadPool(50);

    public static void addWork(Runnable task) {
        workThreadPool.submit(task);
    }
}
