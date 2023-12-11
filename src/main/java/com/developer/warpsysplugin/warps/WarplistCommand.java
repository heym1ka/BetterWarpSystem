package com.developer.warpsysplugin.warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarplistCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("warpsys.list")) {
            if (args.length == 0) {
                File adir = new File("./plugins/WarpSys/warplocations");
                if (!adir.exists()) {
                    adir.mkdirs();
                }
                List<String> warplist = new ArrayList<>();
                File[] dir = new File("./plugins/WarpSys/warplocations").listFiles();
                for (File file : dir) {
                    if (file.isFile()) {
                        warplist.add(file.getName().replace(".yml", ""));
                    }
                }
                String warps = warplist.toString().replace("[", "").replace("]", "");
                if (warplist.isEmpty()) {
                    sender.sendMessage("§6Es wurden noch keine Warps gesetzt");
                } else {
                    sender.sendMessage("§eWarps: §a" + warps);
                }
            } else {
                sender.sendMessage("§4STOP! §cNutze /warp");
            }
        } else {
            sender.sendMessage("§4STOP! §cDu hast keine Berechtigung die warpliste einzusehen!");
        }
        return false;
    }
}

