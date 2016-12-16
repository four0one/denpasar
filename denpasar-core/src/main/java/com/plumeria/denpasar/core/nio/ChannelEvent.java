package com.plumeria.denpasar.core.nio;

import com.plumeria.denpasar.core.ServiceRequest;

/**
 * Created by chenwei on 2016/12/16.
 */
public class ChannelEvent {

    private ServerDispatcher serverDispatcher;

    public ChannelEvent() {

    }

    public ChannelEvent(ServerDispatcher dispatcher) {
        this.serverDispatcher = dispatcher;
    }

    public Object dispatch(ServiceRequest serviceMeta) {
        return serverDispatcher.dispatch(serviceMeta);
    }

}
