package com.plumeria.denpasar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by chenwei on 2016/12/16.
 */
public class RedisUtil {

    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 5;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 5;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;

    private static JedisPool jedisPool;

    public static void initRedisPoolConfig(String address, int port) {
        if (jedisPool != null) {
            return;
        }
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxWaitMillis(MAX_WAIT);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxTotal(MAX_ACTIVE);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, address, port, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String s = jedis.set(key, value);
        } catch (Exception e) {
            log.error("向redis注册服务出错，检查redis是否可用");
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

    }

}
