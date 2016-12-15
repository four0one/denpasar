package com.chenwei.denpasar.core;

import com.chenwei.denpasar.core.nio.NioServiceInterceptor;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceBeanFactory {

    public static Object generateBean(Class service) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{service});
        enhancer.setCallbackFilter(new InterfaceCallBackFilter());
        enhancer.setCallbacks(new Callback[]{new NioServiceInterceptor(), NoOp.INSTANCE});
        //使用非nio方式 callback使用ServiceMethodInterceptor 服务端开多线程处理，客户端单线程方式（考虑用多线程改写，但是意义？）
        return enhancer.create();
    }

    private static class InterfaceCallBackFilter implements CallbackFilter {

        @Override
        public int accept(Method method) {
            String methodName = method.getName();
            if (methodName.equals("finalize")) {
                return 1;
            }
            if (methodName.equals("notify")) {
                return 1;
            }

            if (methodName.equals("hashCode")) {
                return 1;
            }

            if (methodName.equals("clone")) {
                return 1;
            }

            if (methodName.equals("toString")) {
                return 1;
            }

            if (methodName.equals("notifyAll")) {
                return 1;
            }

            if (methodName.equals("wait")) {
                return 1;
            }

            if (methodName.equals("getClass")) {
                return 1;
            }

            if (methodName.equals("notify")) {
                return 1;
            }

            return 0;
        }
    }

}
