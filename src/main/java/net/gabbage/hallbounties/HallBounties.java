package net.gabbage.hallbounties;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class HallBounties extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        final FileConfiguration config = this.getConfig();
        this.getCommand("bounty").setExecutor(new Bounty(this));
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
