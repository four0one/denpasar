package com.plumeria.denpasar.core;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceRoute {

    private String host;

    private int port;

    public ServiceRoute() {

    }

    public ServiceRoute(String host, int port) {
        setHost(host);
        setPort(port);
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
