package com.plumeria.denpasar.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by chenwei on 2016/12/16.
 */
public class ServiceProviderBean implements InitializingBean {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Object service;

    private float version = 0.0f;

    public ServiceProviderBean(){

    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("服务提供者属性注入完毕，现在向注册中心注册");
    }
}
