package dev.larrox.betterwarpsystem.cmd;

import dev.larrox.betterwarpsystem.WarpSysPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.logging.Level;

public class SetWarpCMD implements CommandExecutor {

    private final WarpSysPlugin plugin;

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    public SetWarpCMD(WarpSysPlugin plugin) {
        this.plugin = plugin;
    }

    private File getWarpFile(String name) {
        String folderName = plugin.getConfig().getString("warps.folder", "warplocations");
        File dir = new File(plugin.getDataFolder(), folderName);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                plugin.getLogger().warning("Could not create warps folder: " + dir.getAbsolutePath());
            }
        }
        return new File(dir, name + ".yml");
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

        if (!(sender instanceof Player player)) {
            sender.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.only-player", "Only players can run this command.")));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid-usage", "Invalid usage.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        if (!player.hasPermission("warpsystem.setwarp") && !player.hasPermission("warpsystem.*") && !player.isOp() && !player.hasPermission("*")) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission", "You do not have permission to use this command.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        String warpName = args[0];
        if (!VALID_NAME_PATTERN.matcher(warpName).matches()) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid-usage", "Invalid usage.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        File warpFile = getWarpFile(warpName);
        if (warpFile.exists()) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.warp-exists", "A warp with this name already exists.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        Location location = player.getLocation();

        if (location.getWorld() == null) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "Your world could not be detected.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        warpConfig.set("world", location.getWorld().getName());
        warpConfig.set("X", location.getX());
        warpConfig.set("Y", location.getY());
        warpConfig.set("Z", location.getZ());
        warpConfig.set("Yaw", location.getYaw());
        warpConfig.set("Pitch", location.getPitch());

        try {
            warpConfig.save(warpFile);
            String msgTemplate = plugin.getConfig().getString("messages.warp-set", "Successfully set warp '{warp}'.");
            String msg = msgTemplate.replace("{warp}", warpName);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("color.primary", "&7") + " " + msg));
            player.playSound(player.getLocation(), parseSound("success", Sound.ENTITY_VILLAGER_TRADE), 1f, 1f);

        } catch (IOException e) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "An error occurred.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            plugin.getLogger().log(Level.SEVERE, "Failed to save warp file: " + warpFile.getAbsolutePath(), e);
        }

        return true;
    }
}
