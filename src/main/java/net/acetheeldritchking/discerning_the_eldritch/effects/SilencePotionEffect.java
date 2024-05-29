package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTEPotionEffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SilencePotionEffect extends MobEffect {
    public SilencePotionEffect() {
        super(MobEffectCategory.HARMFUL, 3755355);
    }

    /*@SubscribeEvent
    public static void onLivingTickEvent(TickEvent.PlayerTickEvent event)
    {
        var entity = event.player;
        var level = entity.level;
        var playerMagicData = MagicData.getPlayerMagicData(entity);

        if (entity instanceof ServerPlayer player)
        {
            if (!level.isClientSide)
            {
                if (player.hasEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT.get())
                        && playerMagicData.isCasting())
                {
                    Utils.serverSideCancelCast(player, true);
                }
            }
        }
    }*/
}