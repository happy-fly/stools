package com.kingh.redis;

import com.kingh.config.env.EnvProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Redis 工具类
 *
 * @author 孔冠华
 */
public class RedisOperate {
    private static final Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    private static JedisCluster cluster = null;
    private static Jedis jedis = null;
    private static boolean clusterMode = false;

    private static final String REDIS_CONFIG = EnvProperties.getEnv().getRedisConfig();

    private static Properties prop;

    static {
        prop = new Properties();
        try {
            prop.load(RedisOperate.class.getClassLoader().getResourceAsStream(REDIS_CONFIG));

            String isCluster = prop.getProperty("default.cluster", "true");
            if ("true".equals(isCluster)) {
                clusterMode = true;
            }

            String host = prop.getProperty("default.host");
            String[] hprop = host.split(":");
            if (hprop == null || hprop.length != 2) {
                logger.error("redis 配置文件错误," + host);
                throw new RuntimeException("redis 配置文件错误！");
            }

            if (clusterMode) {
                Set<HostAndPort> node = new HashSet<>();
                node.add(new HostAndPort(hprop[0], Integer.parseInt(hprop[1])));
                // node.add(new HostAndPort("192.168.104.48", 9003));
                // node.add(new HostAndPort("192.168.104.47", 9000));
                // node.add(new HostAndPort("192.168.104.48", 9004));
                // node.add(new HostAndPort("192.168.104.48", 9005));

                cluster = new JedisCluster(node);
                logger.info("redis 为集群模式，JedisCluster实例化成功！");
            } else {
                jedis = new Jedis(hprop[0], Integer.parseInt(hprop[1]));
                logger.info("redis 为单机模式，Jedis实例化成功！");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static void hset(String key, String field, String value) {
        if (clusterMode) {
            cluster.hset(key, field, value);
        } else {
            jedis.hset(key, field, value);
        }
    }

    public static String hget(String key, String field) {
        if (clusterMode) {
            // cluster.
            return cluster.hget(key, field);
        }
        return jedis.hget(key, field);
    }

    public static Long keyslot(String key) {
        if (clusterMode) {
            Map<String, JedisPool> nodes = cluster.getClusterNodes();
            for (Map.Entry<String, JedisPool> en : nodes.entrySet()) {
                JedisPool pool = en.getValue();
                Jedis client = pool.getResource();
                boolean flag = client.exists(key);
                if (flag) {
                    return client.clusterKeySlot(key);
                }
            }
            return -1L;
        } else {
            throw new UnsupportedOperationException("不支持的操作！");
        }
    }

    public static String get(String key) {
        if (clusterMode) {
            return cluster.get(key);
        }
        return jedis.get(key);
    }

    public static void set(String key, String value) {
        if (clusterMode) {
            cluster.set(key, value);
        } else {
            jedis.set(key, value);
        }
    }

    public static void delete(String key) {
        if (clusterMode) {
            cluster.del(key);
        } else {
            jedis.del(key);
        }
    }

    public static Set<String> getHashKey(String key) {
        if (clusterMode) {
            return cluster.hkeys(key);
        }
        return jedis.hkeys(key);
    }

    public static Set<String> keys(String pattern) {
        if (clusterMode) {
            Set<String> keys = new HashSet<>();
            Map<String, JedisPool> nodes = cluster.getClusterNodes();
            for (Map.Entry<String, JedisPool> en : nodes.entrySet()) {
                JedisPool p = en.getValue();
                Jedis client = p.getResource();
                keys.addAll(client.keys(pattern));
            }

            return keys;
        }
        return jedis.keys(pattern);
    }

    public static Set<String> keys() {
        return keys("*");
    }

    public static void hdel(String key, String field) {
        if (clusterMode) {
            cluster.hdel(key, field);
            return;
        }
        jedis.hdel(key, field);
    }

    public static JedisCluster cluster() {
        if (clusterMode) {
            return cluster;
        }
        throw new RuntimeException("当前为客户端模式，不能获取集群操作工具类， 请使用 jedis 方法！");
    }

    public static Jedis jedis() {
        if (clusterMode) {
            throw new RuntimeException("当前为集群模式，不能获取客户端操作工具类，请使用 cluster 方法！");
        }
        return jedis;
    }

    public static Jedis jedis(String source) {
        if (source == null || "".equals(source)) {
            source = "default";
        }
        String isCluster = prop.getProperty(source + ".cluster", "true");
        if ("true".equals(isCluster)) {
            throw new RuntimeException("当前为集群模式，不能获取客户端操作工具类，请使用 cluster 方法！");
        }

        String host = prop.getProperty(source + ".host");
        String[] hprop = host.split(":");
        if (hprop == null || hprop.length != 2) {
            logger.error("redis 配置文件错误," + host);
            throw new RuntimeException("redis 配置文件错误！");
        }

        jedis = new Jedis(hprop[0], Integer.parseInt(hprop[1]));

        String auth = prop.getProperty(source + ".password");
        if (StringUtils.isNoneBlank(auth)) {
            jedis.auth(auth);
        }

        logger.info("redis 为单机模式，Jedis实例化成功！");

        return jedis;
    }

    public static JedisCluster cluster(String source) {

        if (source == null || "".equals(source)) {
            source = "default";
        }

        String isCluster = prop.getProperty(source + ".cluster", "true");
        if ("falase".equals(isCluster)) {
            throw new RuntimeException("当前为客户端模式，不能获取集群操作工具类， 请使用 jedis 方法！");
        }

        String host = prop.getProperty(source + ".host");
        String[] hprop = host.split(":");
        if (hprop == null || hprop.length != 2) {
            logger.error("redis 配置文件错误," + host);
            throw new RuntimeException("redis 配置文件错误！");
        }

        Set<HostAndPort> node = new HashSet<>();
        node.add(new HostAndPort(hprop[0], Integer.parseInt(hprop[1])));

        cluster = new JedisCluster(node);
        logger.info("redis 为集群模式，JedisCluster实例化成功！");

        return cluster;
    }

}
