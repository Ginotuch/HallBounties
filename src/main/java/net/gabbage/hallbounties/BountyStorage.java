package net.gabbage.hallbounties;

import java.util.Map;

public interface BountyStorage {
    String payPlayer(String playerName, String claimedBounty);

    boolean bountyExists(String bountyName);

    void addBounty(String bountyName, Float bountyPrice);

    boolean deleteBounty(String key);

    Map<String, String> getBountyList();
}
