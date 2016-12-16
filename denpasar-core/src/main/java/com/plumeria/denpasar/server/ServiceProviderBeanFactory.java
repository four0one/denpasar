package com.plumeria.denpasar.server;

import com.plumeria.denpasar.core.InitContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * Created by chenwei on 2016/12/16.
 */
public class ServiceProviderBeanFactory extends ApplicationObjectSupport implements FactoryBean, InitializingBean {

    private Object service;

    private float version = 0.0f;

    private ServiceProviderBean providerBean;

    public ServiceProviderBeanFactory() {

    }


    @Override
    public Object getObject() throws Exception {
        return providerBean;
    }

    @Override
    public Class<?> getObjectType() {
        return service.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        providerBean = new ServiceProviderBean();
        providerBean.setVersion(version);
        providerBean.setService(service);
        InitContext initContext = BeanFactoryUtils.beanOfTypeIncludingAncestors(this.getApplicationContext(),
                InitContext.class, true, false);
        initContext.addService(providerBean);
    }



    public void setService(Object service) {
        this.service = service;
    }

    public void setVersion(float version) {
        this.version = version;
    }


}
