package com.plumeria.denpasar.core;

import com.plumeria.denpasar.core.nio.DefaultNioProtocolListener;
import com.plumeria.denpasar.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by chenwei on 2016/12/9.
 */
public class InitContext implements InitializingBean, ApplicationListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, MethodHandler> serviceMap = new HashMap<>();

    //注册中心地址 协议 redis://127.0.0.1:6397://user://password
    private String registry;

    //服务发布协议
    private Protocol protocol;

    private ServiceRegister serviceRegister;


    private final static String REDIS = "redis";

    public InitContext() {
        log.info("初始化initcontext");
    }

    private void initServiceMap() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("service.properties"));
            Set<Object> keySet = properties.keySet();
            if (keySet.size() > 0) {
                for (Object serviceName : keySet) {
                    String implService = (String) properties.get(serviceName);
                    Class<?> serviceClass = Class.forName(implService);
                    Object serviceInstance = serviceClass.newInstance();
                    Method[] methods = serviceClass.getMethods();
                    if (methods == null || methods.length == 0) {
                        continue;
                    }
                    MethodHandler methodHandler = null;
                    for (Method method : methods) {
                        methodHandler = new MethodHandler(serviceInstance, method);
                        String sk = appendServiceKey(serviceName.toString(), method.getName());
                        serviceMap.put(sk, methodHandler);
                    }
                }
            }
            log.debug("{}", serviceMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private String appendServiceKey(String serviceName, String methodName) {
        StringBuffer sk = new StringBuffer();
        sk.append(serviceName).append(".").append(methodName);
        return sk.toString().trim();
    }


    public MethodHandler getHandler(String serviceName, String methodName) {
        MethodHandler handler = serviceMap.get(appendServiceKey(serviceName, methodName));
        return handler;
    }

    public void addService(ServiceProviderBean serviceProviderBean) {
        //注册到本地服务处理handlermap
        Object service = serviceProviderBean.getService();
        Class serviceClass = service.getClass();
        Method[] methods = new Method[]{};
        MethodHandler methodHandler = null;
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            handleCreate(service, serviceClass);
        } else {
            for (Class<?> infClass : interfaces) {
                handleCreate(service, infClass);
            }
        }

        //注册中心
        ServiceRegisteBean serviceRegisteBean = new ServiceRegisteBean();
        serviceRegisteBean.setVersion(serviceProviderBean.getVersion());
        serviceRegisteBean.setAddress(protocol.getAddress());
        serviceRegisteBean.setPort(protocol.getPort());
        serviceRegisteBean.setServiceClassName(service.getClass().getName());
        serviceRegister.register(serviceRegisteBean);
    }

    private void handleCreate(Object service, Class<?> infClass) {
        Method[] methods;
        MethodHandler methodHandler;
        methods = infClass.getMethods();
        for (Method method : methods) {
            methodHandler = new MethodHandler(service, method);
            String sk = appendServiceKey(infClass.getName(), method.getName());
            serviceMap.put(sk, methodHandler);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("context属性注入完毕，初始化内部变量");
        String[] registryElem = registry.split("://");
        if (registryElem == null || registryElem.length == 0) {
            throw new IllegalArgumentException("初始化容器失败，注册中心参数解析错误");
        }

        String target = registryElem[0];
        String addressAndPort = registryElem[1];
        String[] strings = addressAndPort.split(":");
        String address = strings[0];
        int port = Integer.parseInt(strings[1]);

        if (target.equals(REDIS)) {
            serviceRegister = new RedisRegister(address, port);
        }

    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("容器初始化完成，启动监听程序");
        //启动监听
        protocol.getListener().listen(this);
    }

}
