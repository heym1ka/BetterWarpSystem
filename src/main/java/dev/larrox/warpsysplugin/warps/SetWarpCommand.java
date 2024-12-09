package dev.larrox.warpsysplugin.warps;

import dev.larrox.warpsysplugin.WarpSysPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class SetWarpCommand implements CommandExecutor {
    private static final String WARP_DIRECTORY = "./plugins/WarpSysPlugin/warplocations/";
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");
    WarpSysPlugin plugin = WarpSysPlugin.getInstance();
    FileConfiguration config = plugin.getConfig();
    String PrimaryColor = config.getString("color.primary");
    String SecondaryColor = config.getString("color.secondary");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PrimaryColor + " Nur Spieler können diesen Command ausführen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(PrimaryColor + " Ungültiger Warp...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (!player.hasPermission("warpsystem.setwarp") ||
                !player.hasPermission("warpsystem.*") ||
                !player.isOp() ||
                !player.hasPermission("*")) {
            player.sendMessage(PrimaryColor + " Du hast keine Berechtigung, " + SecondaryColor + "Warpset " + PrimaryColor + "zu nutzen.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        String warpName = args[0];
        if (!VALID_NAME_PATTERN.matcher(warpName).matches() || !VALID_NAME_PATTERN.matcher(warpName.toLowerCase()).matches()) {
            player.sendMessage(PrimaryColor + " Ungültiger Warp...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        File warpFile = new File(WARP_DIRECTORY + warpName + ".yml");
        if (warpFile.exists()) {
            player.sendMessage(PrimaryColor + " Dieser Warp existiert bereits...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        Location location = player.getLocation();

        warpConfig.set("world", location.getWorld().getName());
        warpConfig.set("X", location.getX());
        warpConfig.set("Y", location.getY());
        warpConfig.set("Z", location.getZ());
        warpConfig.set("Yaw", location.getYaw());
        warpConfig.set("Pitch", location.getPitch());

        try {
            warpConfig.save(warpFile);
            player.sendMessage(PrimaryColor + " Neuer Warp erfolgreich gesetzt");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);

        } catch (IOException e) {
            player.sendMessage(PrimaryColor + " Es ist ein Fehler aufgetreten...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            e.printStackTrace();
        }

        return true;
    }
}
