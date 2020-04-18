package net.gabbage.hallbounties;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class BountyCommandsImpl implements BountyCommands {
    BountyStorage bountyStorage;

    public BountyCommandsImpl(BountyStorage bountyStorage) {
        this.bountyStorage = bountyStorage;

    }


    @Override
    public void list(CommandSender sender) {
        sender.sendMessage("§2HallBounties:§r");
        int c = 0;
        Map<String, HashMap<String, Object>> bounties = this.bountyStorage.getBountyList();
        SortedSet<String> sortedKeys = new TreeSet<>(bounties.keySet());
        for (String key: sortedKeys) {
            c++;  // haha get it cause it's better than a Sea Bass!
            sender.sendMessage("  - " + key + ": §6$" + bounties.get(key).get("price") + ", amount left: " + bounties.get(key).get("amountLeft"));
        }
        if (c == 0){
            sender.sendMessage("  - No bounties active, sorry!");
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
    public void add(CommandSender sender, String bountyName, String bountyPrice, String quantity) {
        boolean isInt = Util.isInt(quantity);
        boolean isFloat = Util.isFloat(bountyPrice);
        if (!(isInt && isFloat)) {
            if (!isInt) {
                sender.sendMessage("§2HallBounties: §r§c " + quantity + " cannot be read as a number");
            }
            if (!isFloat) {
                sender.sendMessage("§2HallBounties: §r§c " + bountyPrice + " cannot be read as a number");
            }
            return;
        }

        Float price = Float.parseFloat(bountyPrice);
        int amountLeft = Integer.parseInt(quantity);

        if (amountLeft <= 0) {
            sender.sendMessage("§2HallBounties: §r§cChose a number higher than 0");
            return;
        }
        this.bountyStorage.addBounty(bountyName, price, amountLeft);

        sender.sendMessage("§2HallBounties: §r§2Successfully added bounty §n" + bountyName + "§r§2 for §6$" + price);

    }

    @Override
    public void pay(CommandSender sender, String claimedBounty, String playerName, String quantity) {

        if (!Util.isInt(quantity)) {
            sender.sendMessage("§2HallBounties: §r§c " + quantity + " cannot be read as a number");
            return;
        }
        int quantityInt = Integer.parseInt(quantity);

        if (quantityInt <= 0) {
            sender.sendMessage("§2HallBounties: §r§cChose a number higher than 0");
            return;
        }

        int amountLeft;
        Player payee = Bukkit.getServer().getPlayer(playerName);
        if (payee == null) {
            sender.sendMessage("§2HallBounties: §r§cPlayer \"" + playerName + "\" isn't online or doesn't exist");
        } else if (!this.bountyStorage.bountyExists(claimedBounty)) {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + claimedBounty + "§r§c doesn't exist");
        } else if ((amountLeft = this.bountyStorage.bountyAmountLeft(claimedBounty)) < quantityInt) {
            sender.sendMessage("§2HallBounties: §r§cBounty §n" + claimedBounty + "§r§c only has " + amountLeft + " claims left");
        } else {
            String price = this.bountyStorage.payPlayer(playerName, claimedBounty, quantityInt);
            Bukkit.broadcastMessage("§2HallBounties: §r" + payee.getDisplayName() + "§3 been payed §6$" + price + "§r§3 for the bounty §n" + claimedBounty);

        }
    }
}
