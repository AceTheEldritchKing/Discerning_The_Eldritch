package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.IceSpearItem;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.TheSnowgraveItem;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
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
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;
import java.util.logging.Level;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.INSANITY_METER;

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
                String formattedTime = ASUtils.convertTicksToTime(time);

                if (player instanceof ServerPlayer serverPlayer)
                {
                    // display a message to the player
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.silence_warning").append(formattedTime)
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                    serverPlayer.level().playSound(null , player.getX() , player.getY() , player.getZ() ,
                            SoundEvents.FIRE_EXTINGUISH , SoundSource.PLAYERS , 0.5f , 1f);
                }
            }
        }

        // Insanity system
        if (DTEConfig.enableInsanitySystem)
        {
            //System.out.println("System enabled?");

            entity.getData(INSANITY_METER);

            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                //System.out.println("Is Eldritch?");

                if (entity.getData(INSANITY_METER) <= DTEConfig.maxInsanityValue)
                {
                    //System.out.println("Increment?");
                    entity.setData(INSANITY_METER, entity.getData(INSANITY_METER) + 1);

                    //System.out.println("Meter: " + entity.getData(INSANITY_METER));

                    if (entity.getData(INSANITY_METER) == DTEConfig.maxInsanityValue)
                    {
                        //System.out.println("Do effect?");
                        if (entity instanceof ServerPlayer player)
                        {
                            if (player.getRandom().nextFloat() * 100.0F < 35.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_1")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 32.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_2")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 35.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_3")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 5.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_4")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                        }
                    }
                }
            }
        }

        // Otherworldly Presence
        boolean hasPresenceEffect = entity.hasEffect(DTEPotionEffectRegistry.METAPHYSICAL_POTION_EFFECT);
        if (entity instanceof ServerPlayer player && !player.level().isClientSide())
        {
            if (hasPresenceEffect)
            {
                event.setCanceled(true);
                // Effect Duration
                int time2 = player.getEffect(DTEPotionEffectRegistry.METAPHYSICAL_POTION_EFFECT).getDuration();
                // convert duration to time format  using the method convertTicksToTime
                String formattedTime2 = ASUtils.convertTicksToTime(time2);

                if (player instanceof ServerPlayer serverPlayer)
                {
                    // display a message to the player
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.presence_warning").append(formattedTime2)
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                    serverPlayer.level().playSound(null , player.getX() , player.getY() , player.getZ() ,
                            SoundEvents.FIRE_EXTINGUISH , SoundSource.PLAYERS , 0.5f , 1f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(PlayerTickEvent.Post event)
    {
        LivingEntity entity = event.getEntity();

        // Do this every two seconds
        if (entity.getData(INSANITY_METER) >= DTEConfig.maxInsanityValue && entity.tickCount % 100 == 0)
        {
            entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 9, false, false, false));
        }
    }

    @SubscribeEvent
    public static void livingDamageEvent(LivingDamageEvent.Post event)
    {
        var sourceEntity = event.getSource().getEntity();
        var target = event.getEntity();
        var projectile = event.getSource().getDirectEntity();

        // Curios
        if (sourceEntity != null)
        {
            if (sourceEntity instanceof Player player)
            {
                // Diary of Decay
                if (ASUtils.hasCurio(player, ItemRegistries.DIARY_OF_DECAY.get()))
                {
                    if (projectile instanceof Projectile)
                    {
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0, true, true, true));
                    }
                }
            }
        }

        // Other stuff
        if (sourceEntity instanceof LivingEntity livingEntity)
        {
            // Ice Spear && Snowgrave
            ItemStack mainhandItem = livingEntity.getMainHandItem();

            if (mainhandItem.getItem() instanceof IceSpearItem)
            {
                if (target instanceof LivingEntity livingTarget)
                {
                    livingTarget.setTicksFrozen(20*20);
                }
            }
            if (mainhandItem.getItem() instanceof TheSnowgraveItem)
            {
                if (target instanceof LivingEntity livingTarget)
                {
                    livingTarget.setTicksFrozen(50*20);
                }
            }

            // Mend Flesh
            if (livingEntity.hasEffect(DTEPotionEffectRegistry.MEND_FLESH_EFFECT) && DTEConfig.mendFleshLifesteal)
            {
                livingEntity.heal(1.5F);

                int count = 8;
                float radius = 0.25F;

                ASUtils.spawnParticlesInCircle(count, radius, 0.5F, 0.1F, livingEntity, ParticleTypes.SCULK_SOUL);

                if (!livingEntity.level().isClientSide)
                {
                    livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
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
            if (ASUtils.hasCurio(player, ItemRegistries.IRONBOUND_FEATHER.get()))
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

            ASUtils.spawnParticlesInCircle(count, radius, 0.5F, 0.1F, attacker, ParticleTypes.SCULK_SOUL);

            if (!attacker.level().isClientSide)
            {
                attacker.level().playSound(attacker, attacker.blockPosition(), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void entityDamageCapEvent(LivingDamageEvent.Pre event)
    {
        var entity = event.getEntity();
        var source = event.getSource();

        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        {
            if (entity instanceof LivingEntity livingEntity)
            {
                //Ascended One
                if (livingEntity instanceof AscendedOneBoss ascendedOneBoss && DTEConfig.enableAscendedOneDamageCap)
                {
                    float baseDamage = event.getOriginalDamage();
                    float newDamage = ASUtils.basicDamageCap(baseDamage, 0, DTEConfig.ascendedOneDamageCap);
                    event.setNewDamage(newDamage);
                    //DiscerningTheEldritch.LOGGER.debug("Old Damage: " + event.getOriginalDamage());
                    //DiscerningTheEldritch.LOGGER.debug("New Damage: " + event.getNewDamage());
                }

                // Boss Two

                // Boss Three

                // Boss Four

                // Boss Five

                // Boss Six
            }
        }
    }
}
