package com.plumeria.denpasar.server;

import com.alibaba.fastjson.JSONObject;
import com.plumeria.denpasar.util.RedisUtil;

/**
 * Created by chenwei on 2016/12/16.
 */
public class RedisRegister implements ServiceRegister {


    public RedisRegister(String address, int port) {
        RedisUtil.initRedisPoolConfig(address, port);
    }

    @Override
    public void register(ServiceRegisteBean registeBean) {
        String jsonString = JSONObject.toJSONString(registeBean);
        RedisUtil.set(registeBean.key(), jsonString);
    }
}
