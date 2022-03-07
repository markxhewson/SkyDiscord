package xyz.lotho.me.skydiscord.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import xyz.lotho.me.skydiscord.SkyDiscord;
import xyz.lotho.me.skydiscord.utils.Utilities;

import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class RedisManager {

    private SkyDiscord instance;

    private String password;
    private boolean auth;

    private String channel;
    private JedisPool subscriberPool, publisherPool;

    private long lastConnect = -1;

    private JedisPubSub pubsub;
    private JsonParser PARSER = new JsonParser();

    public RedisManager(SkyDiscord instance) {
        this.instance = instance;
    }

    public boolean isRedisConnected() {
        return this.subscriberPool != null && !this.subscriberPool.isClosed() && this.publisherPool != null && !this.publisherPool.isClosed();
    }

    public void connect() {
        String host = this.instance.config.getString("redis.host");
        int port = this.instance.config.getInt("redis.port");

        this.password = this.instance.config.getString("redis.password");
        this.auth = this.password != null && this.password.length() > 0;

        Utilities.log("[REDIS] Attempting to connect to Redis Database (" + host + ":" + port + ")");

        this.channel = "SkyCloud";

        try {
            this.subscriberPool = this.publisherPool = new JedisPool(new JedisPoolConfig(), host, port, 30_000, this.password.isEmpty() ? null : this.password, 0, null);
            this.subscriberPool.getResource();

            try {
                Thread.sleep(1500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception exception) {
            this.subscriberPool = null;
            this.publisherPool = null;

            exception.printStackTrace();
        }

        if (this.isRedisConnected()) {
            Utilities.log("[REDIS] Redis is now connected and sync is ready.");

            this.lastConnect = System.currentTimeMillis();
        }
    }

    public void sendRequest(String channel, JsonObject object) {
        try {
            if (object == null) throw new IllegalStateException("Object being passed into pool was null.");

            try (Jedis jedis = this.publisherPool.getResource()) {
                if (this.auth) jedis.auth(this.password);

                jedis.publish(channel, object.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            this.subscriberPool.close();
            this.publisherPool.close();
            this.pubsub = null;
            this.channel = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
