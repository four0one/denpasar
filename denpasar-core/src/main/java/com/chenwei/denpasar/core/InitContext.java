package com.chenwei.denpasar.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by chenwei on 2016/12/9.
 */
public class InitContext {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, MethodHandler> serviceMap = new HashMap<>();

    public InitContext() {
        initServiceMap();
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


}
