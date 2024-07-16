package dev.larrox.warpsysplugin.warps;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarplistCommand implements CommandExecutor {
    private static final String WARP_DIRECTORY = "./plugins/WarpSys/warplocations/";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§7Nur Spieler Können diesen Command ausführen.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("warpsystem.list") || !player.hasPermission("warpsystem.*")) {
            sender.sendMessage("§7Du hast keine Berechtigung, §aWarplist §7zu nutzen.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage("§7Nutze §a/warplist§7.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        File warpDir = new File(WARP_DIRECTORY);
        if (!warpDir.exists()) {
            warpDir.mkdirs();
        }

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
            sender.sendMessage("§7Keine Warps gefunden...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        } else {
            String warps = String.join("§8,§a ", warpList);
            sender.sendMessage("§7Warps: §a" + warps);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
        }

        return true;
    }
}
