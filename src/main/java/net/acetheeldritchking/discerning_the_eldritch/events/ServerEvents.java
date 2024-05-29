package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.acetheeldritchking.discerning_the_eldritch.registeries.DTEPotionEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {
    // Silence effect preventing spell casting
    @SubscribeEvent
    public static void onLivingTickEvent(SpellPreCastEvent event) {
        var entity = event.getEntity();
        var level = entity.level;
        var playerMagicData = MagicData.getPlayerMagicData(entity);

        if (entity instanceof ServerPlayer player) {
            if (!level.isClientSide) {
                if (player.hasEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT.get()) && playerMagicData.isCasting()) {

                    // Effect Duration
                    int time = player.getEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT.get()).getDuration();

                    // convert duration to time format  using the method convertTicksToTime
                    String formattedTime = convertTicksToTime(time);

                    Utils.serverSideCancelCast(player , true);

                    // display a message to the player
                    player.displayClientMessage(Component.literal(ChatFormatting.BOLD + "Can't cast for : " + formattedTime).withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F))) , true);
                }
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
