package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BountyCommandsImpl implements BountyCommands{
    HallBounties plugin;
    FileConfiguration config;
    ConfigurationSection bounties;

    public BountyCommandsImpl(HallBounties hallBounties) {
        plugin = hallBounties;
        config = this.plugin.getConfig();
        bounties = this.config.getConfigurationSection("bounties");
    }

    private boolean deleteBounty(String key) {
        if (!this.bounties.contains(key)) {
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
    public void list(CommandSender sender) {
        sender.sendMessage("§2HallBounties:§r");
        for (String s : this.bounties.getKeys(true)) {
            sender.sendMessage("  - " + s + ": §6$" + this.bounties.getString(s));
        }
    }

    @Override
    public void remove(CommandSender sender, String bountyName) {
        if (this.deleteBounty(bountyName)) {
            sender.sendMessage("§2HallBounties: §r§2Successfully deleted bounty §n" + bountyName);
        } else {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + bountyName + "§r§c doesn't exist!");
        }
    }

    @Override
    public void add(CommandSender sender, String bountyName, String bountyPrice) {
        boolean isfloat;
        Float price = null;
        try {
            price = Float.parseFloat(bountyPrice);
            isfloat = true;
        } catch (NumberFormatException e) {
            isfloat = false;
            sender.sendMessage(bountyPrice + " cannot be read as a number");
        }
        if (isfloat) {
            Map<String, String> added_bounties = new HashMap<>();

            for (String bounty : this.bounties.getKeys(true)) {
                added_bounties.put(bounty, this.bounties.getString(bounty));
            }

            added_bounties.put(bountyName, price.toString());
            this.config.createSection("bounties", added_bounties);
            this.plugin.saveConfig();
            this.bounties = this.config.getConfigurationSection("bounties");

            sender.sendMessage("§2HallBounties: §r§2Successfully added bounty §n" + bountyName + "§r§2 for §6$" + price.toString());
        }
    }

    @Override
    public void pay(CommandSender sender, String claimedBounty, String playerName) {

        Player payee = Bukkit.getServer().getPlayer(playerName);
        if (payee == null) {
            sender.sendMessage("§2HallBounties: §r§cPlayer \"" + playerName + "\" isn't online or doesn't exist");
        } else if (!this.bounties.contains(claimedBounty)) {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + claimedBounty + "§r§c doesn't exist");
        } else {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String price = this.bounties.getString(claimedBounty);
            String consolecommand = "economy give " + playerName + " " + price;
            Bukkit.dispatchCommand(console, consolecommand);

            this.deleteBounty(claimedBounty);
            Bukkit.broadcastMessage("§2HallBounties: §r" + payee.getDisplayName() + "§3 been payed §6$" + price + "§r§3 for the bounty §n" + claimedBounty);

        }
    }
}
