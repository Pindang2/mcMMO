package com.gmail.nossr50.core.runnables.items;

import com.gmail.nossr50.core.config.MainConfig;
import com.gmail.nossr50.core.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.core.locale.LocaleLoader;
import com.gmail.nossr50.core.mcmmo.entity.Player;
import com.gmail.nossr50.core.mcmmo.item.ItemStack;
import com.gmail.nossr50.core.mcmmo.world.Location;
import com.gmail.nossr50.core.util.ChimaeraWing;
import com.gmail.nossr50.core.util.ItemUtils;
import com.gmail.nossr50.core.util.Misc;
import com.gmail.nossr50.core.util.skills.SkillUtils;

public class ChimaeraWingWarmup extends BukkitRunnable {
    private McMMOPlayer mcMMOPlayer;

    public ChimaeraWingWarmup(McMMOPlayer mcMMOPlayer) {
        this.mcMMOPlayer = mcMMOPlayer;
    }

    @Override
    public void run() {
        checkChimaeraWingTeleport();
    }

    private void checkChimaeraWingTeleport() {
        Player player = mcMMOPlayer.getPlayer();
        Location previousLocation = mcMMOPlayer.getTeleportCommenceLocation();

        if (player.getLocation().distanceSquared(previousLocation) > 1.0 || !player.getInventory().containsAtLeast(ChimaeraWing.getChimaeraWing(0), 1)) {
            player.sendMessage(LocaleLoader.getString("Teleport.Cancelled"));
            mcMMOPlayer.setTeleportCommenceLocation(null);
            return;
        }

        ItemStack inHand = player.getInventory().getItemInMainHand();

        if (!ItemUtils.isChimaeraWing(inHand) || inHand.getAmount() < MainConfig.getInstance().getChimaeraUseCost()) {
            player.sendMessage(LocaleLoader.getString("Skills.NeedMore", LocaleLoader.getString("Item.ChimaeraWing.Name")));
            return;
        }

        long recentlyHurt = mcMMOPlayer.getRecentlyHurt();
        int hurtCooldown = MainConfig.getInstance().getChimaeraRecentlyHurtCooldown();

        if (hurtCooldown > 0) {
            int timeRemaining = SkillUtils.calculateTimeLeft(recentlyHurt * Misc.TIME_CONVERSION_FACTOR, hurtCooldown, player);

            if (timeRemaining > 0) {
                player.sendMessage(LocaleLoader.getString("Item.Injured.Wait", timeRemaining));
                return;
            }
        }

        ChimaeraWing.chimaeraExecuteTeleport();
    }
}