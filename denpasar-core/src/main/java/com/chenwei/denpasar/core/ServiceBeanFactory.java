package com.chenwei.denpasar.core;

import com.chenwei.denpasar.core.nio.NioServiceInterceptor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceBeanFactory {

    public static Object generateBean(Class service) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{service});
        enhancer.setCallback(new NioServiceInterceptor());
        return enhancer.create();
    }

}
