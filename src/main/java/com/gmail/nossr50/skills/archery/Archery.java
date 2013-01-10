package com.gmail.nossr50.skills.archery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.util.Misc;

public class Archery {
    private static AdvancedConfig advancedConfig = AdvancedConfig.getInstance();
    private static List<TrackedEntity> trackedEntities = new ArrayList<TrackedEntity>();

    public static final int ARROW_TRACKING_MAX_BONUS_LEVEL = advancedConfig.getRetrieveMaxBonusLevel();
    public static final int ARROW_TRACKING_MAX_BONUS = advancedConfig.getRetrieveBonusMax();

    public static final int BONUS_DAMAGE_INCREASE_LEVEL = advancedConfig.getSkillShotIncreaseLevel();
    public static final double BONUS_DAMAGE_INCREASE_PERCENT = advancedConfig.getSkillShotIncreasePercentage();
    public static final double BONUS_DAMAGE_MAX_BONUS_PERCENTAGE = advancedConfig.getSkillShotBonusMax();

    public static final int DAZE_MAX_BONUS_LEVEL = advancedConfig.getDazeMaxBonusLevel();
    public static final int DAZE_MAX_BONUS = advancedConfig.getDazeBonusMax();
    public static final int DAZE_MODIFIER = advancedConfig.getDazeModifier();

    protected static void incrementTrackerValue(LivingEntity livingEntity) {
        for (TrackedEntity trackedEntity : trackedEntities) {
            if (trackedEntity.getLivingEntity().getEntityId() == livingEntity.getEntityId()) {
                trackedEntity.incrementArrowCount();
                return;
            }
        }

        addToTracker(livingEntity); //If the entity isn't tracked yet
    }

    protected static void addToTracker(LivingEntity livingEntity) {
        TrackedEntity trackedEntity = new TrackedEntity(livingEntity);

        trackedEntity.incrementArrowCount();
        trackedEntities.add(trackedEntity);
    }

    protected static void removeFromTracker(TrackedEntity trackedEntity) {
        trackedEntities.remove(trackedEntity);
    }

    /**
     * Check for arrow retrieval.
     *
     * @param entity The entity hit by the arrows
     */
    public static void arrowRetrievalCheck(LivingEntity livingEntity) {
        for (Iterator<TrackedEntity> entityIterator = trackedEntities.iterator(); entityIterator.hasNext(); ) {
            TrackedEntity trackedEntity = entityIterator.next();

            if (trackedEntity.getLivingEntity().getEntityId() == livingEntity.getEntityId()) {
                Misc.dropItems(livingEntity.getLocation(), new ItemStack(Material.ARROW), trackedEntity.getArrowCount());
                entityIterator.remove();
                return;
            }
        }
    }
}
