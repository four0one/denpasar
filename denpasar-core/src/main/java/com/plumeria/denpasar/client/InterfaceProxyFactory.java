package com.plumeria.denpasar.client;

import com.plumeria.denpasar.core.ServiceBeanFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by chenwei on 2016/12/15.
 */
public class InterfaceProxyFactory implements FactoryBean {

    //接口类型
    private Class interfaceClass;

    @Override
    public Object getObject() throws Exception {
        return ServiceBeanFactory.generateBean(interfaceClass,1.0f);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
