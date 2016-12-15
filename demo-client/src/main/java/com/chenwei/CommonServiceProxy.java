package com.chenwei;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by chenwei on 2016/12/8.
 */
public class CommonServiceProxy implements InvocationHandler{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.debug("进入代理");
        return null;
    }
}
