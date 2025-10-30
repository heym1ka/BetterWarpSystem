package dev.larrox.betterwarpsystem.cmd;

import dev.larrox.betterwarpsystem.WarpSysPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WarpCMD implements CommandExecutor, TabCompleter {

    private final WarpSysPlugin plugin;

    public WarpCMD(WarpSysPlugin plugin) {
        this.plugin = plugin;
    }

    private File getWarpFolder() {
        String folderName = plugin.getConfig().getString("warps.folder", "warplocations");
        File dir = new File(plugin.getDataFolder(), folderName);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                plugin.getLogger().warning("Could not create warps folder: " + dir.getAbsolutePath());
            }
        }
        return dir;
    }

    private Sound parseSound(String key, Sound fallback) {
        String s = plugin.getConfig().getString("sounds." + key, fallback.name());
        try {
            return Sound.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return fallback;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String primary = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("color.primary", "&7"));

        if (args.length == 0) {
            sender.sendMessage(primary + plugin.getConfig().getString("messages.invalid-usage", "Please provide a warp name."));
            return true;
        }

        String warpName = args[0];
        File warpFolder = getWarpFolder();

        if (warpFolder.exists() && warpFolder.isDirectory()) {
            File[] files = warpFolder.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                File matchingFile = null;

                for (File file : files) {
                    String fileNameWithoutExtension = file.getName().replace(".yml", "");
                    if (fileNameWithoutExtension.equalsIgnoreCase(warpName)) {
                        matchingFile = file;
                        break;
                    }
                }

                if (matchingFile == null) {
                    sender.sendMessage(primary + plugin.getConfig().getString("messages.warp-not-found", "No warp with that name was found."));
                    if (sender instanceof Player p) p.playSound(p.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                    return true;
                }

                YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(matchingFile);
                String worldName = warpConfig.getString("world", "");
                World world = Bukkit.getWorld(worldName);

                if (!(sender instanceof Player player)) {
                    sender.sendMessage(primary + plugin.getConfig().getString("messages.only-player", "Only players can run this command."));
                    return true;
                }


                if (args.length != 1) {
                    player.sendMessage(primary + plugin.getConfig().getString("messages.invalid-usage", "Invalid usage."));
                    player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                    return true;
                }

                if (!matchingFile.exists()) {
                    player.sendMessage(primary + plugin.getConfig().getString("messages.invalid-usage", "Invalid usage."));
                    player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                    return true;
                }

                if (world == null) {
                    player.sendMessage(primary + plugin.getConfig().getString("messages.warp-world-missing", "The world for this warp does not exist."));
                    player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                    return true;
                }

                double x = warpConfig.getDouble("X", 0.0);
                double y = warpConfig.getDouble("Y", 0.0);
                double z = warpConfig.getDouble("Z", 0.0);
                float yaw = (float) warpConfig.getDouble("Yaw", 0.0);
                float pitch = (float) warpConfig.getDouble("Pitch", 0.0);

                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);

                String warpDisplay = matchingFile.getName().replace(".yml", "");

                if (plugin.getConfig().getBoolean("actionbar")) {
                    String template = plugin.getConfig().getString("messages.teleport-actionbar", "You were teleported to '{warp}'");
                    String msg = template.replace("{warp}", warpDisplay);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', msg)));
                }

                if (plugin.getConfig().getBoolean("message")) {
                    String template = plugin.getConfig().getString("messages.teleport", "You have been teleported to '{warp}'.");
                    String coloredWarp = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("color.secondary", "&a") + warpDisplay + plugin.getConfig().getString("color.primary", "&7"));
                    String msg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("color.primary", "&7") + " " + template.replace("{warp}", coloredWarp));
                    player.sendMessage(msg);
                }

                if (plugin.getConfig().getBoolean("sound")) {
                    player.playSound(player.getLocation(), parseSound("success", Sound.ENTITY_VILLAGER_TRADE), 1f, 1f);
                }

            } else {
                plugin.getLogger().log(Level.WARNING, "No YAML files found in warp folder: " + warpFolder.getAbsolutePath());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "No YAML files found in warps folder.")));
            }
        } else {
            plugin.getLogger().log(Level.WARNING, "Warp directory does not exist: " + warpFolder.getAbsolutePath());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "The warp directory does not exist.")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            File folder = getWarpFolder();
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