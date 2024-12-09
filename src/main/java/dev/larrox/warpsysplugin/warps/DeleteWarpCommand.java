package dev.larrox.warpsysplugin.warps;

import dev.larrox.warpsysplugin.WarpSysPlugin;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteWarpCommand implements CommandExecutor, TabCompleter {
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
        if (!player.hasPermission("warpsystem.delete") ||
                !player.hasPermission("warpsystem.*") ||
                !player.isOp() ||
                !player.hasPermission("*")) {
            player.sendMessage(PrimaryColor + " Du hast keine Berechtigung, " + SecondaryColor + "WarpRemove " + PrimaryColor + "zu nutzen");
            return true;
        }

        String warpName = args[0];
        File warpFile = new File(WARP_DIRECTORY + warpName + ".yml");

        if (args.length != 1) {
            player.sendMessage(PrimaryColor + " Ungültiger Warp...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (!warpFile.exists()) {
            player.sendMessage(PrimaryColor + " Dieser Warp existiert nicht...");
            return true;
        }

        if (warpFile.delete()) {
            player.sendMessage(PrimaryColor + " Warp erfolgreich gelöscht");
        } else {
            player.sendMessage(PrimaryColor + " Es ist ein Fehler aufgetreten");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            File folder = new File(WARP_DIRECTORY);
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
