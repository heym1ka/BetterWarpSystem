package dev.larrox.warpsysplugin.warps;

import dev.larrox.warpsysplugin.WarpSysPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarplistCommand implements CommandExecutor {
    private static final String WARP_DIRECTORY = "./plugins/WarpSysPlugin/warplocations/";
    WarpSysPlugin plugin = WarpSysPlugin.getInstance();
    String PrimaryColor = plugin.getConfig().getString("color.primary");
    String SecondaryColor = plugin.getConfig().getString("color.secondary");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PrimaryColor + " Nur Spieler können diesen Command ausführen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 0) {
            sender.sendMessage(PrimaryColor + " Nutze " + SecondaryColor + "/warplist" + PrimaryColor + ".");
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
            sender.sendMessage(PrimaryColor + " Keine Warps gefunden...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        } else {
            String warps = String.join(SecondaryColor + "§8," + SecondaryColor + " ", warpList);
            sender.sendMessage(PrimaryColor + " Warps: " + SecondaryColor + warps);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
        }

        return true;
    }
}
