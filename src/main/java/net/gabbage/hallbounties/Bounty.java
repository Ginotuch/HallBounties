package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Bounty implements CommandExecutor {
    HallBounties plugin;
    FileConfiguration config;
    ConfigurationSection bounties;

    public Bounty(HallBounties hallBounties) {
        plugin = hallBounties;
        config = this.plugin.getConfig();
        bounties = this.config.getConfigurationSection("bounties");
    }

    public boolean deleteBounty(String key) {
        if (this.bounties.contains(key)) {
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
        } else {
            return false;
        }
    }

    public void remove(CommandSender sender, String bountyName){
        if (this.deleteBounty(bountyName)){
            sender.sendMessage("§2HallBounties: §r§2Successfully deleted bounty §n" + bountyName);
        }
        else {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + bountyName + "§r§c doesn't exist!");
        }
    }


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

    public void pay(CommandSender sender, String claimedBounty, String playerName) {

        Player payee = Bukkit.getServer().getPlayer(playerName);
        if (payee == null) {
            sender.sendMessage("§2HallBounties: §r§cPlayer \"" + playerName + "\" isn't online or doesn't exist");
        } else if (!this.bounties.contains(claimedBounty)) {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + claimedBounty + " doesn't exist");
        } else {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String price = this.bounties.getString(claimedBounty);
            String consolecommand = "economy give " + playerName + " " + price;
            Bukkit.dispatchCommand(console, consolecommand);

            this.deleteBounty(claimedBounty);
            Bukkit.broadcastMessage("§2HallBounties: §r" + payee.getDisplayName() + " been payed §6$" + price + "§r for the bounty §n" + claimedBounty);

        }
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("hallbounties.use")) {
            if (args.length < 1) {
                sender.sendMessage("§2HallBounties: §r\n Usages:\n  /bounty list\n  /bounty add <name> <price>\n  /bounty pay <bounty_name> <player_name>\n  /bounty remove <bounty_name>");
            } else if (args[0].toLowerCase().equals("list")) {
                sender.sendMessage("§2HallBounties:§r");
                for (String s : this.bounties.getKeys(true)) {
                    sender.sendMessage("  - " + s + ": §6$" + this.bounties.getString(s));
                }
            } else if (args[0].toLowerCase().equals("add") && sender.hasPermission("hallbounties.add")) {
                if (args.length != 3) {
                    sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty add <name> <price>");
                } else {
                    this.add(sender, args[1], args[2]);
                }
            } else if (args[0].toLowerCase().equals("pay") && sender.hasPermission("hallbounties.pay")) {
                if (args.length != 3) {
                    sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty pay <bounty_name> <name>");
                } else {
                    this.pay(sender, args[1], args[2]);
                }
            } else if (args[0].toLowerCase().equals("remove") && sender.hasPermission("hallbounties.remove")) {
                if (args.length != 2) {
                    sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty remove <bounty_name>");
                } else {
                    this.remove(sender, args[1]);
                }

            } else {
                sender.sendMessage("§2HallBounties: §r§cYou don't have permission for this");
            }
        }
        return true;
    }
}