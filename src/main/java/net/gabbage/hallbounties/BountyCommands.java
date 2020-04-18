package net.gabbage.hallbounties;

import org.bukkit.command.CommandSender;

public interface BountyCommands {
    void list(CommandSender sender);

    void remove(CommandSender sender, String bountyName);

    void add(CommandSender sender, String bountyName, String bountyPrice);

    void pay(CommandSender sender, String claimedBounty, String playerName);
}
