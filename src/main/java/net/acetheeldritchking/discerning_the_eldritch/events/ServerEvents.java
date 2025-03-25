package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.IceSpearItem;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.logging.Level;

import static net.acetheeldritchking.discerning_the_eldritch.utils.DTEUtils.hasCurio;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void onPlayerCastEvent(SpellPreCastEvent event)
    {
        var entity = event.getEntity();
        boolean hasSilenceEffect = entity.hasEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT);
        if (entity instanceof ServerPlayer player && !player.level().isClientSide())
        {
            if (hasSilenceEffect)
            {
                event.setCanceled(true);
                // Effect Duration
                int time = player.getEffect(DTEPotionEffectRegistry.SILENCE_POTION_EFFECT).getDuration();
                // convert duration to time format  using the method convertTicksToTime
                String formattedTime = convertTicksToTime(time);

                if (player instanceof ServerPlayer serverPlayer)
                {
                    // display a message to the player
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal(ChatFormatting.BOLD + "Unable to cast for : " + formattedTime)
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                    serverPlayer.level().playSound(null , player.getX() , player.getY() , player.getZ() ,
                            SoundEvents.FIRE_EXTINGUISH , SoundSource.PLAYERS , 0.5f , 1f);
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

    @SubscribeEvent
    public static void livingDamageEvent(LivingDamageEvent.Post event)
    {
        var sourceEntity = event.getSource().getEntity();
        var target = event.getEntity();
        var projectile = event.getSource().getDirectEntity();

        // Diary of Decay
        if (sourceEntity != null)
        {
            if (sourceEntity instanceof Player player)
            {
                if (hasCurio(player, ItemRegistries.DIARY_OF_DECAY.get()))
                {
                    if (projectile instanceof Projectile)
                    {
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0, true, true, true));
                    }
                }
            }
        }

        if (sourceEntity instanceof LivingEntity livingEntity)
        {
            // Ice Spear
            ItemStack mainhandItem = livingEntity.getMainHandItem();

            if (mainhandItem.getItem() instanceof IceSpearItem)
            {
                if (target instanceof LivingEntity livingTarget)
                {
                    livingTarget.setTicksFrozen(20*20);
                }
            }
        }
    }

    @SubscribeEvent
    public static void livingIncomingDamageEvent(LivingIncomingDamageEvent event)
    {
        var target = event.getEntity();
        var attacker = event.getSource().getEntity();

        // Otherworldly Presence
        if (target instanceof LivingEntity livingTarget)
        {
            if (livingTarget.hasEffect(DTEPotionEffectRegistry.METAPHYSICAL_POTION_EFFECT))
            {
                event.setCanceled(true);
            }
        }

        if (attacker instanceof LivingEntity livingAttacker)
        {
            if (livingAttacker.hasEffect(DTEPotionEffectRegistry.METAPHYSICAL_POTION_EFFECT))
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onFallEvent(LivingFallEvent event)
    {
        var entity = event.getEntity();

        if (entity instanceof Player player)
        {
            if (DTEUtils.hasCurio(player, ItemRegistries.IRONBOUND_FEATHER.get()))
            {
                event.setCanceled(true);
            }
        }
    }
}
