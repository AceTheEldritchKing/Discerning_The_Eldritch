package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTEPotionEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {
    // Silence effect preventing spell casting
    @SubscribeEvent
    public static void onLivingTickEvent(SpellPreCastEvent event) {
        var entity = event.getEntity();
        if (entity instanceof ServerPlayer player && !player.level.isClientSide) {
            boolean hasSilenceEffect = player.hasEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT.get());

            if (hasSilenceEffect) {
                event.setCanceled(true);
                // Effect Duration
                int time = player.getEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT.get()).getDuration();
                // convert duration to time format  using the method convertTicksToTime
                String formattedTime = convertTicksToTime(time);
                // display a message to the player
                player.displayClientMessage(Component.literal(ChatFormatting.BOLD + "Unable to cast for : " + formattedTime)
                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F))) , true);
                player.level.playSound(null , player.getX() , player.getY() , player.getZ() ,
                        SoundEvents.FIRE_EXTINGUISH , SoundSource.PLAYERS , 0.5f , 1f);
            }
        }

    }
    public static String convertTicksToTime(int ticks) {
        // Convert ticks to seconds
        int totalSeconds = ticks / 20;

        // Calculate minutes and seconds
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        // Format the result as mm:ss
        return String.format("%02d:%02d" , minutes , seconds);
    }
}
