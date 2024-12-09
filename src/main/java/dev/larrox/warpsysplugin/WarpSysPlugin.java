package dev.larrox.warpsysplugin;

import dev.larrox.warpsysplugin.cmd.ReloadCMD;
import dev.larrox.warpsysplugin.util.ConfigChange;
import dev.larrox.warpsysplugin.warps.DeleteWarpCommand;
import dev.larrox.warpsysplugin.warps.WarpCommand;
import dev.larrox.warpsysplugin.warps.WarplistCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import dev.larrox.warpsysplugin.warps.SetWarpCommand;

import java.io.File;

public final class WarpSysPlugin extends JavaPlugin {

    public static WarpSysPlugin instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("delwarp").setExecutor(new DeleteWarpCommand());
        getCommand("warps").setExecutor(new WarplistCommand());
        getCommand("warpsysreloadconfig").setExecutor(new ReloadCMD());

        getConfig().addDefault("actionbar", true);
        getConfig().addDefault("message", true);
        getConfig().addDefault("sound", true);
        getConfig().addDefault("color.primary", "§7");
        getConfig().addDefault("color.secondary", "§a");
        getConfig().options().copyDefaults(true);

        saveConfig();

        getServer().getScheduler().runTaskTimer(this, new ConfigChange(this), 0L, 100L);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void loadconfig() {
        File file = new File("./plugins/WarpSysPlugin/config.yml");
        YamlConfiguration yfile = YamlConfiguration.loadConfiguration(file);
    }

    public static WarpSysPlugin getInstance() {
        return instance;
    }


}
