package com.plumeria.denpasar.server;

/**
 * Created by chenwei on 2016/12/16.
 */
public class ServiceRegisteBean {

    private String serviceClassName;

    //ip
    private String address;

    private int port;

    private float version;

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    /**
     * @return serviceClass:version
     */
    public String key() {
        return this.serviceClassName + "" + this.version;
    }
}
