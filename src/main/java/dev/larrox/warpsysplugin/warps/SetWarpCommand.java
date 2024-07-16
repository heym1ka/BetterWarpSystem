package dev.larrox.warpsysplugin.warps;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.bukkit.Effect.Type.SOUND;

public class SetWarpCommand implements CommandExecutor {
    private static final String WARP_DIRECTORY = "./plugins/WarpSys/warplocations/";
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z]+");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§7Nur Spieler Können diesen Command ausführen.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("warpsystem.setwarp") ||!player.hasPermission("warpsystem.*")) {
            player.sendMessage("§7Du hast keine Berechtigung, §aWarpset §7zu nutzen.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        String warpName = args[0].toLowerCase();
        if (!VALID_NAME_PATTERN.matcher(warpName).matches() || args.length != 1) {
            player.sendMessage("§7Ungültiger Warp...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        File warpFile = new File(WARP_DIRECTORY + warpName + ".yml");
        if (warpFile.exists()) {
            player.sendMessage("§7Dieser Warp Existiert bereits...");
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
            player.sendMessage("§7Neuer Warp erfolgreich gesetzt");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);

        } catch (IOException e) {
            player.sendMessage("§cEs ist ein fehler aufgetreten...");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            e.printStackTrace();
        }

        return true;
    }
}
