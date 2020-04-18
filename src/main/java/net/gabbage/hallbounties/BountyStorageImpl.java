package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class BountyStorageImpl implements BountyStorage {
    HallBounties plugin;
    FileConfiguration config;
    ConfigurationSection bounties;

    public BountyStorageImpl(HallBounties hallBounties){
        this.plugin = hallBounties;
        this.config = this.plugin.getConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
    }

    @Override
    public String payPlayer(String playerName, String claimedBounty) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String price = this.bounties.getString(claimedBounty);
        String consolecommand = "economy give " + playerName + " " + price;
        Bukkit.dispatchCommand(console, consolecommand);
        this.deleteBounty(claimedBounty);
        return price;
    }

    @Override
    public boolean bountyExists(String bountyName) {
        return this.bounties.contains(bountyName);
    }

    @Override
    public void addBounty(String bountyName, Float bountyPrice) {
        Map<String, String> added_bounties = new HashMap<>();

        for (String bounty : this.bounties.getKeys(true)) {
            added_bounties.put(bounty, this.bounties.getString(bounty));
        }

        added_bounties.put(bountyName, bountyPrice.toString());
        this.config.createSection("bounties", added_bounties);
        this.plugin.saveConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
    }

    @Override
    public boolean deleteBounty(String key) {
        if (!this.bounties.contains(key, true)) {
            return false;
        }
        Map<String, String> updated_bounties = new HashMap<>();


        for (String bounty : this.bounties.getKeys(true)) {
            if (!bounty.equals(key)) {
                updated_bounties.put(bounty, this.bounties.getString(bounty));
            }
        }
        this.config.createSection("bounties", updated_bounties);
        this.plugin.saveConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
        return true;
    }

    @Override
    public Map<String, String> getBountyList() {
        Map<String, String> bountyList = new HashMap<>();
        for (String s : this.bounties.getKeys(true)){
            bountyList.put(s, this.bounties.getString(s));
        }
        return bountyList;
    }
}
