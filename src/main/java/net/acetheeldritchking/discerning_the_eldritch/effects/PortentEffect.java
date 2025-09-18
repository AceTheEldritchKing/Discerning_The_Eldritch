package net.acetheeldritchking.discerning_the_eldritch.effects;

import net.acetheeldritchking.aces_spell_utils.network.AddShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.network.RemoveShaderEffectPacket;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class PortentEffect extends MobEffect {
    public PortentEffect() {
        super(MobEffectCategory.BENEFICIAL, 1710620);
    }

    /*@SubscribeEvent
    public static void addShader(MobEffectEvent.Added event)
    {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(DTEPotionEffectRegistry.PORTENT_EFFECT) && entity instanceof ServerPlayer serverPlayer)
        {
            DiscerningTheEldritch.LOGGER.debug("Added");
            PacketDistributor.sendToPlayer(serverPlayer, new AddShaderEffectPacket(DiscerningTheEldritch.MOD_ID, "shaders/grayscale_darker.json"));
        }
    }*/

    // We're managing removal of the shader here because adding is extremely inconsistent
    // If the player stops casting the spell, the shader effect can still be removed
    @SubscribeEvent
    public static void removeShader(MobEffectEvent.Remove event)
    {
        LivingEntity entity = event.getEntity();
        Holder<MobEffect> effect = event.getEffect();

        if (effect instanceof PortentEffect)
        {
            if (entity.hasEffect(effect) && entity instanceof ServerPlayer serverPlayer)
            {
                DiscerningTheEldritch.LOGGER.debug("Removed - Removed");
                PacketDistributor.sendToPlayer(serverPlayer, new RemoveShaderEffectPacket());
            }
        }
    }

    @SubscribeEvent
    public static void removeShader(MobEffectEvent.Expired event)
    {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(DTEPotionEffectRegistry.PORTENT_EFFECT) && entity instanceof ServerPlayer serverPlayer)
        {
            DiscerningTheEldritch.LOGGER.debug("Removed - Expired");
            PacketDistributor.sendToPlayer(serverPlayer, new RemoveShaderEffectPacket());
        }
    }
}
