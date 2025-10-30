package dev.larrox.betterwarpsystem.cmd;

import dev.larrox.betterwarpsystem.WarpSysPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteWarpCMD implements CommandExecutor, TabCompleter {

    private final WarpSysPlugin plugin;

    private final Map<UUID, PendingDelete> pending = new ConcurrentHashMap<>();

    private static class PendingDelete {
        final String warpName;
        final long expiresAt;
        PendingDelete(String warpName, long expiresAt) {
            this.warpName = warpName;
            this.expiresAt = expiresAt;
        }
    }

    public DeleteWarpCMD(WarpSysPlugin plugin) {
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

        if (!(sender instanceof Player player)) {
            sender.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.only-player", "Only players can run this command.")));
            return true;
        }

        if (!player.hasPermission("warpsystem.delete") && !player.hasPermission("warpsystem.*") && !player.isOp() && !player.hasPermission("*")) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission", "You do not have permission to use this command.")));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid-usage", "Usage: /delwarp <name>")));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("confirm")) {
            String warpName = args[1];
            PendingDelete p = pending.get(player.getUniqueId());
            if (p == null || !p.warpName.equalsIgnoreCase(warpName) || p.expiresAt < System.currentTimeMillis()) {
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.delete-cancelled", "Deletion cancelled or timeout.")));
                player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                pending.remove(player.getUniqueId());
                return true;
            }

            File warpFile = new File(getWarpFolder(), warpName + ".yml");
            if (!warpFile.exists()) {
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.warp-not-found", "This warp does not exist.")));
                pending.remove(player.getUniqueId());
                player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
                return true;
            }

            boolean deleted = warpFile.delete();
            pending.remove(player.getUniqueId());
            if (deleted) {
                String template = plugin.getConfig().getString("messages.warp-deleted", "Warp '{warp}' deleted successfully.");
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', template.replace("{warp}", warpName)));
                player.playSound(player.getLocation(), parseSound("success", Sound.ENTITY_VILLAGER_TRADE), 1f, 1f);
            } else {
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "An error occurred.")));
                player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            }

            return true;
        }

        String warpName = args[0];
        File warpFile = new File(getWarpFolder(), warpName + ".yml");

        if (!warpFile.exists()) {
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.warp-not-found", "This warp does not exist.")));
            player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            return true;
        }

        boolean confirmationEnabled = plugin.getConfig().getBoolean("confirmation.delete.enabled", true);
        int ttl = plugin.getConfig().getInt("confirmation.delete.ttl_seconds", 20);

        if (confirmationEnabled) {
            long expiresAt = System.currentTimeMillis() + (ttl * 1000L);
            pending.put(player.getUniqueId(), new PendingDelete(warpName, expiresAt));
            String template = plugin.getConfig().getString("messages.delete-confirm", "Please confirm deletion by running: /delwarp confirm {warp} within {seconds}s.");
            String message = template.replace("{warp}", warpName).replace("{seconds}", String.valueOf(ttl));
            player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', message));
            player.playSound(player.getLocation(), parseSound("delete_confirm", Sound.BLOCK_ANVIL_PLACE), 1f, 1f);
            return true;
        } else {
            if (warpFile.delete()) {
                String template = plugin.getConfig().getString("messages.warp-deleted", "Warp '{warp}' deleted successfully.");
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', template.replace("{warp}", warpName)));
                player.playSound(player.getLocation(), parseSound("success", Sound.ENTITY_VILLAGER_TRADE), 1f, 1f);
            } else {
                player.sendMessage(primary + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error", "An error occurred.")));
                player.playSound(player.getLocation(), parseSound("fail", Sound.ENTITY_VILLAGER_NO), 1f, 1f);
            }
            return true;
        }
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