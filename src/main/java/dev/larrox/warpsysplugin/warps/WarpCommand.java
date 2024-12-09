package dev.larrox.warpsysplugin.warps;

import dev.larrox.warpsysplugin.WarpSysPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private static final String WARP_FOLDER = "./plugins/WarpSysPlugin/warplocations/";
    WarpSysPlugin plugin = WarpSysPlugin.getInstance();
    FileConfiguration config = plugin.getConfig();
    String PrimaryColor = config.getString("color.primary");
    String SecondaryColor = config.getString("color.secondary");


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(PrimaryColor + " Bitte gib einen Warp-Namen an.");
            return true;
        }

        String warpName = args[0];
        File warpFolder = new File(WARP_FOLDER);

        if (warpFolder.exists() && warpFolder.isDirectory()) {
            File[] files = warpFolder.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files != null) {
                File matchingFile = null;

                for (File file : files) {
                    String fileNameWithoutExtension = file.getName().replace(".yml", "");
                    if (fileNameWithoutExtension.equalsIgnoreCase(warpName)) {
                        matchingFile = file;
                        break;
                    }
                }

                // Wenn keine passende Datei gefunden wurde
                if (matchingFile == null) {
                    sender.sendMessage(PrimaryColor + " Kein Warp mit diesem Namen gefunden.");
                    return true;
                }

                // YAML-Datei laden und Warp-Daten extrahieren
                YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(matchingFile);
                String worldName = warpConfig.getString("world", "");
                World world = Bukkit.getWorld(worldName);

                if (!(sender instanceof Player)) {
                    sender.sendMessage(PrimaryColor + " Nur Spieler können diesen Command ausführen.");
                    return true;
                }

                Player player = (Player) sender;

                // Wenn der Warp ungültig ist
                if (args.length != 1) {
                    player.sendMessage(PrimaryColor + " Ungültiger Warp...");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                if (!matchingFile.exists()) {
                    player.sendMessage(PrimaryColor + "Ungültiger Warp...");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                if (world == null) {
                    player.sendMessage(PrimaryColor + " Die Welt des Warps wurde gelöscht oder existiert nicht...");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    return true;
                }

                // Teleportationsdaten laden
                double x = warpConfig.getDouble("X", 0.0);
                double y = warpConfig.getDouble("Y", 0.0);
                double z = warpConfig.getDouble("Z", 0.0);
                float yaw = (float) warpConfig.getDouble("Yaw", 0.0);
                float pitch = (float) warpConfig.getDouble("Pitch", 0.0);

                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);


                if (config.getBoolean("actionbar")) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PrimaryColor + " Du wurdest zu §8'" + SecondaryColor + matchingFile.getName().replace(".yml", "") + "§8'" + PrimaryColor + " teleportiert."));
                }

                if (config.getBoolean("message")) {
                    player.sendMessage("§7Du wurdest zu §8'§a" + matchingFile.getName().replace(".yml", "") + "§8'§7 teleportiert.");
                }

                if (config.getBoolean("sound")) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
                }

            } else {
                sender.sendMessage("§7Keine YAML-Dateien im Verzeichnis gefunden.");
            }
        } else {
            sender.sendMessage("§7Das Verzeichnis existiert nicht.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            File folder = new File(WARP_FOLDER);
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
