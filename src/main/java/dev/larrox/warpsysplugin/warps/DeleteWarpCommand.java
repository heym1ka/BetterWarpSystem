package dev.larrox.warpsysplugin.warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class DeleteWarpCommand implements CommandExecutor {
    private static final String WARP_DIRECTORY = "./plugins/WarpSys/warplocations/";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4STOP! §cNur Spieler können diesen Befehl ausführen!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("warpsystem.delete") && !player.hasPermission("warpsystem.*")) {
            player.sendMessage("§4STOP! §cDu hast keine Berechtigung, Warps zu löschen!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§4STOP! §cGebe bitte einen Warp-Namen an!\n§a/delwarp <warpname>");
            return true;
        }

        String warpName = args[0].toLowerCase();
        File warpFile = new File(WARP_DIRECTORY + warpName + ".yml");

        if (!warpFile.exists()) {
            player.sendMessage("§4STOP! §cDieser Warp existiert nicht");
            return true;
        }

        if (warpFile.delete()) {
            player.sendMessage("§aWarp erfolgreich gelöscht!");
        } else {
            player.sendMessage("§4STOP! §cEin Fehler ist aufgetreten, der Warp konnte nicht gelöscht werden.");
        }

        return true;
    }
}
