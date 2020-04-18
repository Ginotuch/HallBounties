package net.gabbage.hallbounties;

import java.util.HashMap;
import java.util.Map;

public interface BountyStorage {
    String payPlayer(String playerName, String claimedBounty, Integer quantity);

    boolean bountyExists(String bountyName);

    void addBounty(String bountyName, Float bountyPrice, Integer abountLeft);

    boolean deleteBounty(String key);

    int bountyAmountLeft(String bountyName);

    Map<String, HashMap<String, Object>> getBountyList();
}
