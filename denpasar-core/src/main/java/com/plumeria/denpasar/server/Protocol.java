package com.plumeria.denpasar.server;

import com.plumeria.denpasar.core.InitContext;
import com.plumeria.denpasar.core.nio.DefaultNioProtocolListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chenwei on 2016/12/16.
 * 服务发布协议
 */
public class Protocol {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;

    private String address;

    private int port;

    private static final String PROTOCOL_DEFAULT = "default";
    private static final String PROTOCOL_RMI = "rmi";

    public Protocol() throws UnknownHostException {
        InetAddress ia = InetAddress.getLocalHost();
        this.address = ia.getHostAddress();
        logger.debug("本机ip:{}", this.address);
    }

    public ProtocolListener getListener() {
        ProtocolListener protocolListener = (context) -> logger.info("空白监听启动");
        //服务发布方式，default是nio 其余rmi。。。
        if (this.getName().equals(PROTOCOL_DEFAULT)) {
            protocolListener = new DefaultNioProtocolListener(this);
        }

        return protocolListener;
    }

    /*public void listen() {
        ProtocolListener protocolListener = port -> {
            logger.info("空白监听启动");
        };
        //服务发布方式，default是nio 其余rmi。。。
        if (this.getName().equals(PROTOCOL_DEFAULT)) {
            protocolListener = new DefaultNioProtocolListener();
        }
        protocolListener.listen(this.port);
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
