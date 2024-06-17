package com.developer.warpsysplugin.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("warpsystem.usewarps") || p.hasPermission("warpsystem.*")) {
                if (args.length == 1) {
                    File file = new File("./plugins/WarpSys/warplocations/" + args[0].toLowerCase() + ".yml");
                    if (file.exists()) {
                        YamlConfiguration yfile = YamlConfiguration.loadConfiguration(file);
                        World w = Bukkit.getWorld(yfile.getString("World", "world"));

                        if (w != null) {
                            double X = yfile.getDouble("X", 0.0);
                            double Y = yfile.getDouble("Y", 0.0);
                            double Z = yfile.getDouble("Z", 0.0);
                            float Yaw = (float) yfile.getDouble("Yaw", 0.0);
                            float Pitch = (float) yfile.getDouble("Pitch", 0.0);
                            Location loc = new Location(w, X, Y, Z, Yaw, Pitch);

                            p.teleport(loc);
                            p.sendMessage("§aDu wurdest Teleportiert!");
                        } else {
                            p.sendMessage("§4STOP! §cDie Welt des Warps existiert nicht!");
                        }
                    } else {
                        p.sendMessage("§4STOP! §cDer Warp existiert nicht!");
                    }
                } else {
                    p.sendMessage("§4STOP! §cBitte gebe ein warp namen ein! \n§a/warp §2<warpname>");
                }
            } else {
                p.sendMessage("§4STOP! §cDu hast keine Berechtigung Warps zu nutzen!");
            }
        } else {
            sender.sendMessage("§4STOP! §cDu musst ein Spieler sein!");
        }
        return true;
    }
}
