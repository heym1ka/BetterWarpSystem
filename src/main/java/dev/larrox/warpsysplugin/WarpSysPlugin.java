package dev.larrox.warpsysplugin;

import dev.larrox.warpsysplugin.warps.DeleteWarpCommand;
import dev.larrox.warpsysplugin.warps.WarpCommand;
import dev.larrox.warpsysplugin.warps.WarplistCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import dev.larrox.warpsysplugin.warps.SetWarpCommand;

import java.io.File;

public final class WarpSysPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        loadconfig();
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("delwarp").setExecutor(new DeleteWarpCommand());
        getCommand("warps").setExecutor(new WarplistCommand());
    }

    public void loadconfig() {
        File file = new File("./plugins/WarpSys/config.yml");
        YamlConfiguration yfile = YamlConfiguration.loadConfiguration(file);
    }
}
