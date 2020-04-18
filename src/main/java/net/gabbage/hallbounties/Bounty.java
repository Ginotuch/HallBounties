package net.gabbage.hallbounties;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Bounty implements CommandExecutor {
    public static final String PERMISSION_USE = "hallbounties.use";
    public static final String PERMISSION_ADD = "hallbounties.add";
    public static final String PERMISSION_PAY = "hallbounties.pay";
    public static final String PERMISSION_REMOVE = "hallbounties.remove";
    BountyCommands bountyCommands;

    public Bounty(BountyCommands bountyCommands) {
        this.bountyCommands = bountyCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION_USE)) {
            sender.sendMessage("§2HallBounties: §r§cYou don't have permission for this");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§2HallBounties: §r\n Usages:\n  /bounty list\n  /bounty add <name> <price> [quantity]\n  /bounty pay <bounty_name> <player_name> [quantity]\n  /bounty remove <bounty_name>");
            return true;
        }

        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("list")) {
            this.bountyCommands.list(sender);
        } else if (subCommand.equalsIgnoreCase("add") && sender.hasPermission(PERMISSION_ADD)) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty add <name> <price> [quantity]");
            } else {
                String amount = "1";
                if (args.length == 4) {
                    amount = args[3];
                }
                this.bountyCommands.add(sender, args[1], args[2], amount);
            }
        } else if (subCommand.equalsIgnoreCase("pay") && sender.hasPermission(PERMISSION_PAY)) {
            if (args.length != 3 && args.length != 4) {
                sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty pay <bounty_name> <name> [quantity]");
            } else {
                String amount = "1";
                if (args.length == 4) {
                    amount = args[3];
                }
                this.bountyCommands.pay(sender, args[1], args[2], amount);
            }
        } else if (subCommand.equalsIgnoreCase("remove") && sender.hasPermission(PERMISSION_REMOVE)) {
            if (args.length != 2) {
                sender.sendMessage("§2HallBounties: §rIncorrect argument count.\n  Usage: /bounty remove <bounty_name>");
            } else {
                this.bountyCommands.remove(sender, args[1]);
            }

        }
        return true;
    }
}