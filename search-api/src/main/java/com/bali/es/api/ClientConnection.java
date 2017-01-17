package com.bali.es.api;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by chenwei on 2017/1/16.
 */
public class ClientConnection {

    @Value("${es.cluster.ip}")
    private String clusterAddress;

    @Value("${es.cluster.port}")
    private int clusterPort;

    public void connect() {
        Client client = new TransportClient().addTransportAddress(
                new InetSocketTransportAddress(clusterAddress, clusterPort));
    }

}
