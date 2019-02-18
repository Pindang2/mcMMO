package com.gmail.nossr50.core.runnables;

import com.gmail.nossr50.core.data.UserManager;
import com.gmail.nossr50.core.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.core.party.PartyManager;
import com.gmail.nossr50.core.runnables.player.PlayerProfileSaveTask;

public class SaveTimerTask extends BukkitRunnable {
    @Override
    public void run() {
        // All player data will be saved periodically through this
        int count = 1;

        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
            new PlayerProfileSaveTask(mcMMOPlayer.getProfile()).runTaskLaterAsynchronously(mcMMO.p, count);
            count++;
        }

        PartyManager.saveParties();
    }
}