package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTEPotionEffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {
    // Silence effect preventing spell casting
    @SubscribeEvent
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
    }
}
