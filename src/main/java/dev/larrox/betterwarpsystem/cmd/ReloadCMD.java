package dev.larrox.betterwarpsystem.cmd;

import dev.larrox.betterwarpsystem.WarpSysPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class ReloadCMD implements CommandExecutor {

    private final WarpSysPlugin plugin;

    public ReloadCMD(WarpSysPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            plugin.reloadConfig();
            String msg = plugin.getConfig().getString("messages.reloaded", "&aConfiguration reloaded.");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
    }
}
