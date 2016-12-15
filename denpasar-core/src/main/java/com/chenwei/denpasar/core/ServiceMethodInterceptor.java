package com.chenwei.denpasar.core;

import com.chenwei.denpasar.util.ByteReader;
import com.chenwei.denpasar.util.SerializeUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceMethodInterceptor implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<ServiceRoute> routes = new ArrayList<>();

    {
        routes.add(new ServiceRoute("127.0.0.1", 8281));
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {

        if (method.getName().equals("finalize")) {
            Object result = null;
            try {
                result = methodProxy.invokeSuper(o, objects);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return result;
        }

        Random random = new Random();
        int i = random.nextInt(routes.size());
        ServiceRoute serviceRoute = routes.get(i);
        log.debug("使用路由：{}", i);

        ServiceRequest request = new ServiceRequest();
        request.setArgs(objects);
        request.setServiceName(o.getClass().getInterfaces()[0].getName());
        request.setMethodName(method.getName());

        Object result = null;
        try {
            Socket clientSocket = new Socket(serviceRoute.getHost(), serviceRoute.getPort());
            log.debug("clientSocket连接成功");
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(SerializeUtil.serialize(request));
            outputStream.flush();
            clientSocket.shutdownOutput();

            log.debug("clientSocket发送数据成功");
            InputStream inputStream = clientSocket.getInputStream();
            byte[] bytes = ByteReader.readToByte(inputStream);
            result = SerializeUtil.deserialize(bytes);
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.debug("clientSocket接受数据成功");
        return result;
    }


}
