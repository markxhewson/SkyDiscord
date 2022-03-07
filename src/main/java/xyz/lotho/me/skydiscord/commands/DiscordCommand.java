package xyz.lotho.me.skydiscord.commands;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skydiscord.SkyDiscord;
import xyz.lotho.me.skydiscord.utils.Utilities;

import java.util.Random;

public class DiscordCommand implements CommandExecutor {

    private final SkyDiscord instance;

    public DiscordCommand(SkyDiscord instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        String code = "#" + new Random().nextInt(80000) + "SC";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "discordLinkPacket");
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("uuid", player.getUniqueId().toString());
        jsonObject.addProperty("displayName", player.getName());

        this.instance.redisManager.sendRequest("SkyCloud", jsonObject);

        String message = Utilities.colorize("\n&eGenerating code...\n &7&oYour verification code is &a" + code + "&7&o, type /link <code> in\n &7&odiscord to complete the verification process!\n ");
        player.spigot().sendMessage(TextComponent.fromLegacyText(message));

        return true;
    }
}
