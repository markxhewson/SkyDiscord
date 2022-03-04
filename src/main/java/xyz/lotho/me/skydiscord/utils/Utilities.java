package xyz.lotho.me.skydiscord.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Utilities {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Collection<? extends Player> getOnline() {
        return Bukkit.getServer().getOnlinePlayers();
    }

    public static void log(String message) {
       System.out.println("[LOG] " + message);
    }
}
