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

    public BountyStorageImpl(HallBounties hallBounties) {
        this.plugin = hallBounties;
        this.config = this.plugin.getConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
    }

    @Override
    public String payPlayer(String playerName, String claimedBounty, Integer quantity) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String price = this.bounties.getConfigurationSection(claimedBounty).getString("price");
        Float amountPayed = (Float.parseFloat(price) * quantity);
        String consolecommand = "economy give " + playerName + " " + amountPayed;
        Bukkit.dispatchCommand(console, consolecommand);
        Integer amountLeft = this.bounties.getConfigurationSection(claimedBounty).getInt("amountLeft") - quantity;
        this.deleteBounty(claimedBounty);
        if (amountLeft > 0) {
            this.addBounty(claimedBounty, Float.parseFloat(price), amountLeft);
        }
        return amountPayed.toString();
    }

    @Override
    public boolean bountyExists(String bountyName) {
        return this.bounties.contains(bountyName, true);
    }

    @Override
    public void addBounty(String bountyName, Float bountyPrice, Integer amountLeft) {
        Map<String, HashMap<String, Object>> added_bounties = this.getBountyList();

        HashMap<String, Object> newBounty = new HashMap<>();
        newBounty.put("price", bountyPrice);
        newBounty.put("amountLeft", amountLeft);

        added_bounties.put(bountyName, newBounty);
        this.config.createSection("bounties", added_bounties);
        this.plugin.saveConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
    }

    @Override
    public boolean deleteBounty(String key) {
        if (!this.bounties.contains(key, true)) {
            return false;
        }
        Map<String, HashMap<String, Object>> updated_bounties = this.getBountyList();
        updated_bounties.remove(key);
        this.config.createSection("bounties", updated_bounties);
        this.plugin.saveConfig();
        this.bounties = this.config.getConfigurationSection("bounties");
        return true;
    }

    @Override
    public int bountyAmountLeft(String bountyName) {
        return this.bounties.getConfigurationSection(bountyName).getInt("amountLeft");
    }

    @Override
    public Map<String, HashMap<String, Object>> getBountyList() {
        Map<String, HashMap<String, Object>> bountyList = new HashMap<>();
        for (String bountyName : this.bounties.getKeys(false)) {
            HashMap<String, Object> bounty = new HashMap<>();
            bounty.put("price", Float.parseFloat(this.bounties.getConfigurationSection(bountyName).getString("price")));
            bounty.put("amountLeft", this.bounties.getConfigurationSection(bountyName).getInt("amountLeft"));
            bountyList.put(bountyName, bounty);
        }
        return bountyList;
    }
}
