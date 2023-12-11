package com.developer.warpsysplugin.warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class DeleteWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("warpsys.delete")) {
            if (args.length == 1) {
                File file = new File("./plugins/WarpSys/warplocations" + File.separator + args[0].toLowerCase() + ".yml");
                if (file.exists()) {
                    file.delete();
                    sender.sendMessage("§aWarp Gelöscht!");
                } else {
                    sender.sendMessage("§4STOP! §cDieser Warp existiert nicht");
                }
            } else {
                sender.sendMessage("§4STOP! §cgebe bitte einen Warp namen an!\n§a/delwarp <warpname>");
            }
            return true;
        }
        return false;
    }
}
