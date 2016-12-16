package com.plumeria.denpasar.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenwei on 2016/12/9.
 */
public class MethodHandler {

    private Object service;

    private Method method;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MethodHandler(Object service, Method method) {
        setMethod(method);
        setService(service);
    }

    public Object handle(Object[] args) {
        try {
            Object result = method.invoke(service, args);
            logger.info("{}+{}={}",args[0],args[1],result);
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
