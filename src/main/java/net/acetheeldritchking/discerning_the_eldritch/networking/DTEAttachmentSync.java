package net.acetheeldritchking.discerning_the_eldritch.networking;

import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.minecraft.world.entity.LivingEntity;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.DEVOURED_ENTITIES;

public class DTEAttachmentSync {
    /***
     * Setters & Getters
     */
    // Devouring
    public static int getDevour(LivingEntity entity)
    {
        return entity.getData(DEVOURED_ENTITIES);
    }

    public static void setDevour(int val, LivingEntity entity)
    {
        entity.getData(DEVOURED_ENTITIES);
        entity.setData(DTEAttachmentRegistry.DEVOURED_ENTITIES, entity.getData(DEVOURED_ENTITIES) + val);
    }

    public static void resetDevour(LivingEntity entity)
    {
        entity.getData(DEVOURED_ENTITIES);
        entity.setData(DTEAttachmentRegistry.DEVOURED_ENTITIES, 0);
    }
}
