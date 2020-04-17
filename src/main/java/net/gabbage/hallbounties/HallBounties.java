package net.gabbage.hallbounties;

import org.bukkit.plugin.java.JavaPlugin;

public final class HallBounties extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("bounty").setExecutor(new Bounty(this));

    }

    @Override
    public void onDisable() {
    }
}
