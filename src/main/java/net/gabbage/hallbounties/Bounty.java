package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Bounty implements CommandExecutor {
    HallBounties plugin;
    FileConfiguration config;

    public Bounty(HallBounties hallBounties) {
        plugin = hallBounties;
        config = this.plugin.getConfig();
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("hallbounties.use")) {
            if (args.length < 1) {
                sender.sendMessage("Usages:\n /bounty list\n /bounty add <name> <price>\n /bounty pay <player_name>\n /bounty remove <bounty_name>");
            } else if (args[0].toLowerCase().equals("list")) {
                List<String> bounties = this.config.getStringList("bounties");
                sender.sendMessage("Bounties:");
                for (String s : bounties) {
                    if (this.config.contains(s)) {
                        sender.sendMessage("  - " + s + ": " + config.getString(s));
                    }
                }
            } else if (args[0].toLowerCase().equals("add")) {
                if (args.length != 3) {
                    sender.sendMessage("Incorrect argument count.\nUsage: /bounty add <name> <price>");
                } else {
                    boolean isfloat;
                    Float price = null;
                    try {
                        price = Float.parseFloat(args[2]);
                        isfloat = true;
                    } catch (NumberFormatException e) {
                        isfloat = false;
                        sender.sendMessage(args[2] + " cannot be read as a number.");
                    }
                    if (isfloat) {
                        this.config.set(args[1], price.toString());
                        List<String> temp_bounties = this.config.getStringList("bounties");
                        if (!temp_bounties.contains(args[1])) {
                            temp_bounties.add(args[1]);
                        }
                        this.config.set("bounties", temp_bounties);
                        this.plugin.saveConfig();
                    }
                }
            } else if (args[0].toLowerCase().equals("pay")) {
                if (args.length != 3) {
                    sender.sendMessage("Incorrect argument count.\nUsage: /bounty pay <name> <bounty_name>");
                } else {
                    Player payee = Bukkit.getServer().getPlayer(args[1]);
                    List<String> temp_bounties = this.config.getStringList("bounties");
                    if (payee == null) {
                        sender.sendMessage("Player \"" + args[1] + "\" isn't online or doesn't exist");
                    } else if (!temp_bounties.contains(args[2])) {
                        sender.sendMessage("Bounty \"" + args[2] +"\" doesn't exist");
                    }
                    else {
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String consolecommand = "economy give " + args[1] + " " + this.config.getString(args[2]);
                        Bukkit.dispatchCommand(console, consolecommand);
                    }
                }
            }

        } else {
            sender.sendMessage("&4You don't have permission to use this &4(You also shouldn't see this message)");
        }
        return true;
    }
}