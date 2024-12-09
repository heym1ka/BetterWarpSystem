package dev.larrox.warpsysplugin.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigChange implements Runnable {

    private final JavaPlugin plugin;
    private final File configFile;
    private long lastModified;

    public ConfigChange(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.lastModified = configFile.lastModified();
    }

    @Override
    public void run() {
        if (configFile.exists() && configFile.lastModified() != lastModified) {
            lastModified = configFile.lastModified();
            plugin.reloadConfig();
            plugin.getLogger().info("Die Konfiguration wurde automatisch neu geladen!");
        }
    }
}