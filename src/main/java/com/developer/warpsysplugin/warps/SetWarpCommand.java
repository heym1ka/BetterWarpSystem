package com.developer.warpsysplugin.warps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("warpsystem.setwarp") || p.hasPermission("warpsystem.*")){
                if(args.length  == 1) {
                    if(Pattern.matches("[a-zA-Z]+", args[0].toLowerCase())){
                        File File = new File("./plugins/WarpSys/warplocations/" + args[0].toLowerCase() + ".yml");

                        if (!File.exists()) {
                            YamlConfiguration yfile = YamlConfiguration.loadConfiguration(File);
                            Location lo = p.getLocation();

                            yfile.set("world", lo.getWorld().getName());
                            yfile.set("X", lo.getX());
                            yfile.set("Y", lo.getY());
                            yfile.set("Z", lo.getZ());
                            yfile.set("Yaw", lo.getYaw());
                            yfile.set("Pitch", lo.getPitch());
                            try {
                                yfile.save(File);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            p.sendMessage("§aWarp Erfolgreich gesetzt!");
                        } else {
                            p.sendMessage("§4STOP! §cDieser Warp Existiert bereits!");
                        }

                    } else {
                        p.sendMessage("§4STOP! §cEs dürfen nur Buchstaben verwendet werden!");
                    }

                } else {
                    p.sendMessage("§4STOP! §cDu hast den namen Vergessen! \n§a/setwarp §2<warpname>");
                }

            } else {
                sender.sendMessage("§4STOP! §cDu hast keine Berechtigung Warps zu setzen!");
            }
        } else {
            sender.sendMessage("§4STOP! §cDu bist kein Spieler!");
        }

        return true;
    }
}
