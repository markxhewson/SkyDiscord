package xyz.lotho.me.skydiscord;

import com.google.gson.JsonObject;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.skydiscord.redis.RedisManager;
import xyz.lotho.me.skydiscord.utils.Config;

import java.lang.reflect.InvocationTargetException;

public final class SkyDiscord extends JavaPlugin {
    public Config configManager = new Config(this, "config.yml");
    public YamlConfiguration config = configManager.getConfig();

    public RedisManager redisManager;

    @Override
    public void onEnable() {
       this.redisManager = new RedisManager(this);
       this.redisManager.connect();

       /*
        this.getServer().getScheduler().runTaskLater(this, () -> {
           JsonObject jsonObject = new JsonObject();
           jsonObject.addProperty("id", "globalServerAnnouncement");
           jsonObject.addProperty("server", "hub-1");
           jsonObject.addProperty("content", "&8[&c&lSKYCLOUD&8] &eThe server will be undergoing maintenance in the next few moments, you will not be able to reconnect.");

           this.redisManager.sendRequest("SkyCloud", jsonObject);
       }, 100);
        */

    }

    @Override
    public void onDisable() {
        this.redisManager.close();
    }
}
