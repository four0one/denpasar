package com.chenwei.denpasar.core.nio;

import com.chenwei.denpasar.core.ServiceRequest;
import com.chenwei.denpasar.core.ServiceRoute;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by chenwei on 2016/12/8.
 */
public class NioServiceInterceptor implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ExecutorService threadPool;

    private List<ServiceRoute> routes = new ArrayList<>();

    {
        routes.add(new ServiceRoute("127.0.0.1", 8281));
        threadPool = Executors.newFixedThreadPool(5);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        Random random = new Random();
        int i = random.nextInt(routes.size());
        ServiceRoute serviceRoute = routes.get(i);
        log.debug("使用路由：{}", i);

        ServiceRequest request = new ServiceRequest();
        request.setArgs(objects);
        request.setServiceName(o.getClass().getInterfaces()[0].getName());
        request.setMethodName(method.getName());
        Future future = threadPool.submit(new ClientCallbackFuture(request, serviceRoute));
        try {
            return future.get();
        } catch (InterruptedException e) {
            log.info("发生异常");
        } catch (ExecutionException e) {
            log.error("发生异常");
        }
        return null;
    }


}
