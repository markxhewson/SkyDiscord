package xyz.lotho.me.skydiscord;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.skydiscord.commands.DiscordCommand;
import xyz.lotho.me.skydiscord.redis.RedisManager;
import xyz.lotho.me.skydiscord.utils.Config;

public final class SkyDiscord extends JavaPlugin {
    public Config configManager = new Config(this, "config.yml");
    public YamlConfiguration config = configManager.getConfig();

    public RedisManager redisManager;

    @Override
    public void onEnable() {
       this.redisManager = new RedisManager(this);
       this.redisManager.connect();

       this.getCommand("discord").setExecutor(new DiscordCommand(this));
    }

    @Override
    public void onDisable() {
        this.redisManager.close();
    }
}
