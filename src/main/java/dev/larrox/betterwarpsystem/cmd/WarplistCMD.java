package dev.larrox.betterwarpsystem.cmd;

import dev.larrox.betterwarpsystem.WarpSysPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarplistCMD implements CommandExecutor {

    private final WarpSysPlugin plugin;

    public WarplistCMD(WarpSysPlugin plugin) {
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
        String secondary = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("color.secondary", "&a"));

        if (!(sender instanceof Player player)) {
            sender.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.only-player", "Only players can run this command.")));
            return true;
        }


        if (args.length != 0) {
            sender.sendMessage(primary + plugin.getConfig().getString("messages.invalid-usage", "Usage: /warps"));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        File warpDir = getWarpFolder();

        List<String> warpList = new ArrayList<>();
        File[] files = warpDir.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    warpList.add(file.getName().replace(".yml", ""));
                }
            }
        }

        if (warpList.isEmpty()) {
            sender.sendMessage(primary + plugin.getConfig().getString("messages.warps-list-empty", "No warps found."));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
        } else {
            String warps = String.join(secondary + ", " + secondary + " ", warpList);
            String template = plugin.getConfig().getString("messages.warps-list-title", "Warps: {list}");
            String msg = template.replace("{list}", warps);
            sender.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', msg));
            player.playSound(player.getLocation(), parseSound("success", Sound.ENTITY_VILLAGER_TRADE), 1f, 1f);
        }

        return true;
    }
}