package dev.larrox.warpsysplugin.warps;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private static final String WARP_FOLDER = "./plugins/WarpSys/warplocations/";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String warpName = args[0].toLowerCase();
        File warpFile = new File(WARP_FOLDER + warpName + ".yml");

        YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        String worldName = warpConfig.getString("world", "");
        World world = Bukkit.getWorld(worldName);

        if (!(sender instanceof Player)) {
            sender.sendMessage("§7Nur Spieler Können diesen Command ausführen.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("warpsystem.usewarps") || !player.hasPermission("warpsystem.*")) {
            player.sendMessage("§7Du hast keine Berechtigung, §aWarps §7zu nutzen.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (!warpFile.exists() || args.length != 1) {
            player.sendMessage("§7Ungültiger Warp...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (world == null) {
            player.sendMessage("§7Die Welt des Warps wurde gelöscht oder existiert nicht...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        double x = warpConfig.getDouble("X", 0.0);
        double y = warpConfig.getDouble("Y", 0.0);
        double z = warpConfig.getDouble("Z", 0.0);
        float yaw = (float) warpConfig.getDouble("Yaw", 0.0);
        float pitch = (float) warpConfig.getDouble("Pitch", 0.0);

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Du wurdest zu §8'§a" + warpName.toUpperCase() + "§8'§7 Teleportiert."));
        player.sendMessage("§7Du wurdest zu §8'§a" + warpName.toUpperCase() + "§8'§7 Teleportiert.");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            File folder = new File(WARP_FOLDER);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
                if (files != null) {
                    for (File file : files) {
                        completions.add(file.getName().replace(".yml", ""));
                    }
                }
            }
        }

        List<String> result = new ArrayList<>();
        String argument = args[args.length - 1].toLowerCase();
        for (String completion : completions) {
            if (completion.toLowerCase().startsWith(argument)) {
                result.add(completion);
            }
        }
        return result;
    }
}
