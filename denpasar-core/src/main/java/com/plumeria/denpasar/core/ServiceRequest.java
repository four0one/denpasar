package com.plumeria.denpasar.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by chenwei on 2016/12/8.
 */
public class ServiceRequest implements Serializable{

    private String serviceName;

    private String methodName;

    private Object[] args;

    private float version;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", version=" + version +
                '}';
    }
}
