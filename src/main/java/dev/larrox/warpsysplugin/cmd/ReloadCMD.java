package dev.larrox.warpsysplugin.cmd;

import dev.larrox.warpsysplugin.WarpSysPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCMD implements CommandExecutor {

    WarpSysPlugin plugin = WarpSysPlugin.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            plugin.reloadConfig();
            sender.sendMessage("§aDie Konfiguration wurde neu geladen!");
            return true;
    }
}
