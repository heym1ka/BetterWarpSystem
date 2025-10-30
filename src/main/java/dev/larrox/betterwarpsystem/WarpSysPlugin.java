package dev.larrox.betterwarpsystem;

import dev.larrox.betterwarpsystem.cmd.ReloadCMD;
import dev.larrox.betterwarpsystem.cmd.DeleteWarpCMD;
import dev.larrox.betterwarpsystem.cmd.WarpCMD;
import dev.larrox.betterwarpsystem.cmd.WarplistCMD;
import org.bukkit.plugin.java.JavaPlugin;
import dev.larrox.betterwarpsystem.cmd.SetWarpCMD;

public final class WarpSysPlugin extends JavaPlugin {

    public static WarpSysPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        if (getCommand("setwarp") != null) getCommand("setwarp").setExecutor(new SetWarpCMD(this));
        if (getCommand("warp") != null) getCommand("warp").setExecutor(new WarpCMD(this));
        if (getCommand("delwarp") != null) getCommand("delwarp").setExecutor(new DeleteWarpCMD(this));
        if (getCommand("warps") != null) getCommand("warps").setExecutor(new WarplistCMD(this));
        if (getCommand("warpsysreloadconfig") != null) getCommand("warpsysreloadconfig").setExecutor(new ReloadCMD(this));

        getConfig().addDefault("actionbar", true);
        getConfig().addDefault("sound", true);
        getConfig().addDefault("message", true);
        getConfig().addDefault("color.primary", "&7");
        getConfig().addDefault("color.secondary", "&a");
        getConfig().addDefault("warps.folder", "warplocations");

        getConfig().addDefault("messages.reloaded", "&aConfiguration reloaded.");
        getConfig().addDefault("messages.only-player", "&cOnly players can run this command.");
        getConfig().addDefault("messages.no-permission", "&cYou do not have permission to use this command.");
        getConfig().addDefault("messages.invalid-usage", "&cInvalid usage.");
        getConfig().addDefault("messages.warp-exists", "&cA warp with this name already exists.");
        getConfig().addDefault("messages.warp-set", "&aSuccessfully set warp '{warp}'.");
        getConfig().addDefault("messages.warp-deleted", "&aWarp '{warp}' deleted successfully.");
        getConfig().addDefault("messages.warp-not-found", "&cNo warp with that name was found.");
        getConfig().addDefault("messages.warp-world-missing", "&cThe world for this warp does not exist.");
        getConfig().addDefault("messages.warps-list-empty", "&cNo warps found.");
        getConfig().addDefault("messages.warps-list-title", "&7Warps: {list}");
        getConfig().addDefault("messages.teleport", "&7You have been teleported to '&a{warp}&7'.");
        getConfig().addDefault("messages.teleport-actionbar", "You were teleported to '{warp}'");
        getConfig().addDefault("messages.error", "&cAn error occurred.");

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

    }
}
