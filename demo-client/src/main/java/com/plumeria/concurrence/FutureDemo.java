package com.plumeria.concurrence;

import java.util.concurrent.*;

/**
 * Created by chenwei on 2016/12/14.
 */
public class FutureDemo {

    public static void main(String[] args) {
        FutureDemo demo = new FutureDemo();
        Future o = demo.callbackResult();
        System.out.println("主线程处理...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程处理结束");
        try {
            System.out.println(o.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public Future callbackResult() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        Future future = threadPool.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("任务处理...");
                Thread.sleep(30000);
                return 1;
            }
        });

        System.out.println("任务处理结束");
        return future;
    }

}
