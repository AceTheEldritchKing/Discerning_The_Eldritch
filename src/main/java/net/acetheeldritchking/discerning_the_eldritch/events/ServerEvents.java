package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.IceSpearItem;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEConfig;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.logging.Level;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.INSANITY_METER;
import static net.acetheeldritchking.discerning_the_eldritch.utils.DTEUtils.hasCurio;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void onPlayerCastEvent(SpellPreCastEvent event)
    {
        var entity = event.getEntity();
        var spell = SpellRegistry.getSpell(event.getSpellId());

        // Silence
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

        // Insanity system
        // WIP, doesn't work
        /*
        if (DTEConfig.enableInsanitySystem)
        {
            System.out.println("System enabled?");

            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                if (entity.getData(INSANITY_METER) <= DTEConfig.maxInsanityValue)
                {
                    System.out.println("Increment?");
                    entity.setData(INSANITY_METER, entity.getData(INSANITY_METER) + 1);

                    if (entity.getData(INSANITY_METER) >= DTEConfig.maxInsanityValue)
                    {
                        System.out.println("Do effect?");
                        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS));
                    }
                }
            }
        }
        */
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

            // Mend Flesh
            if (livingEntity.hasEffect(DTEPotionEffectRegistry.MEND_FLESH_EFFECT) && DTEConfig.mendFleshLifesteal)
            {
                livingEntity.heal(1.5F);

                int count = 8;
                float radius = 0.25F;

                DTEUtils.spawnParticlesInCircle(count, radius, 0.5F, 0.1F, livingEntity, ParticleTypes.SCULK_SOUL);

                livingEntity.level().playSound(livingEntity, BlockPos.containing(livingEntity.position()), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
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

    @SubscribeEvent
    public static void onExperienceGainedEvent(PlayerXpEvent.XpChange event)
    {
        LivingEntity attacker = event.getEntity();

        // Mend Flesh
        if (attacker.hasEffect(DTEPotionEffectRegistry.MEND_FLESH_EFFECT) && DTEConfig.mendFleshEXPGain)
        {
            float experienceGained = (float) event.getAmount() / 2;

            attacker.heal(experienceGained);

            int count = 8;
            float radius = 0.25F;

            DTEUtils.spawnParticlesInCircle(count, radius, 0.5F, 0.1F, attacker, ParticleTypes.SCULK_SOUL);

            attacker.level().playSound(attacker, BlockPos.containing(attacker.position()), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
