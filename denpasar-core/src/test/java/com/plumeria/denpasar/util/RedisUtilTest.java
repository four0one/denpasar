package com.plumeria.denpasar.util;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

/**
 * Created by chenwei on 2016/12/16.
 */
public class RedisUtilTest {

    @Test
    public void initRedisPoolConfig() throws Exception {
        RedisUtil.initRedisPoolConfig("127.0.0.1", 6379);
        RedisUtil.set("a", "an");
    }

    @Test
    public void set() throws Exception {
        Jedis redis = new Jedis("127.0.0.1", 6379, 10000);
        redis.set("b", "bw");
    }

}