package com.plumeria.denpasar.core;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceBeanFactory {

    public static Object generateBean(Class service, Float version) {
        //服务端不指定，默认版本0，其他情况按照版本号请求
        InterfaceProxyBean proxyBean = new InterfaceProxyBean(service, version == null ? 0 : version);
        return proxyBean.getProxy();
    }
}
