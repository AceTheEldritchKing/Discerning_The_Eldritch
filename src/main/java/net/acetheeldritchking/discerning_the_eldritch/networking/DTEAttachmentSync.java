package net.acetheeldritchking.discerning_the_eldritch.networking;

import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.DEVOURED_ENTITIES;

public class DTEAttachmentSync {
    /***
     * Attachments
     */
    public static int insanityMeter = MinecraftInstanceHelper.getPlayer().getData(DTEAttachmentRegistry.INSANITY_METER);
    public static int frostbiteMeter = MinecraftInstanceHelper.getPlayer().getData(DTEAttachmentRegistry.FROSTBITE_LEVEL);
    public static int devourerMeter = MinecraftInstanceHelper.getPlayer().getData(DTEAttachmentRegistry.DEVOURED_ENTITIES);

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
