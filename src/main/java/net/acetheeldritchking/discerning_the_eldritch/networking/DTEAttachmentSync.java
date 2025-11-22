package net.acetheeldritchking.discerning_the_eldritch.networking;

import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
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
    public static int getDevour()
    {
        return devourerMeter;
    }

    public static void setDevour(int val)
    {
        Player player = MinecraftInstanceHelper.getPlayer();
        player.getData(DEVOURED_ENTITIES);
        player.setData(DTEAttachmentRegistry.DEVOURED_ENTITIES, player.getData(DEVOURED_ENTITIES) + val);
    }

    public static void resetDevour()
    {
        Player player = MinecraftInstanceHelper.getPlayer();
        player.getData(DEVOURED_ENTITIES);
        player.setData(DTEAttachmentRegistry.DEVOURED_ENTITIES, 0);
    }
}
