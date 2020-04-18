package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BountyCommandsImpl implements BountyCommands{
    BountyStorage bountyStorage;

    public BountyCommandsImpl(BountyStorage bountyStorage) {
        this.bountyStorage = bountyStorage;

    }



    @Override
    public void list(CommandSender sender) {
        sender.sendMessage("§2HallBounties:§r");
        for (Map.Entry<String, String> entry : this.bountyStorage.getBountyList().entrySet()) {
            sender.sendMessage("  - " + entry.getKey() + ": §6$" + entry.getValue());
        }
    }

    @Override
    public void remove(CommandSender sender, String bountyName) {
        if (this.bountyStorage.deleteBounty(bountyName)) {
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
            this.bountyStorage.addBounty(bountyName, price);

            sender.sendMessage("§2HallBounties: §r§2Successfully added bounty §n" + bountyName + "§r§2 for §6$" + price.toString());
        }
    }

    @Override
    public void pay(CommandSender sender, String claimedBounty, String playerName) {

        Player payee = Bukkit.getServer().getPlayer(playerName);
        if (payee == null) {
            sender.sendMessage("§2HallBounties: §r§cPlayer \"" + playerName + "\" isn't online or doesn't exist");
        } else if (!this.bountyStorage.bountyExists(claimedBounty)) {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + claimedBounty + "§r§c doesn't exist");
        } else {
            String price = this.bountyStorage.payPlayer(playerName, claimedBounty);
            Bukkit.broadcastMessage("§2HallBounties: §r" + payee.getDisplayName() + "§3 been payed §6$" + price + "§r§3 for the bounty §n" + claimedBounty);

        }
    }
}
