package net.acetheeldritchking.discerning_the_eldritch.events;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import it.crystalnest.prometheus.api.Fire;
import it.crystalnest.prometheus.api.FireManager;
import it.crystalnest.soul_fire_d.fire.FireRegistry;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.blade_of_rancor.BladeOfRancorProjectile;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile.CataclysmBladeSmallProjectile;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.gore_bile.GoreBileAoE;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.DiaryOfDecaySpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.StormWeaverTomeSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.*;
import net.acetheeldritchking.discerning_the_eldritch.networking.DTEAttachmentSync;
import net.acetheeldritchking.discerning_the_eldritch.networking.devour.GetSyncDevourStacksPacket;
import net.acetheeldritchking.discerning_the_eldritch.networking.devour.ResetSyncDevourStacksPacket;
import net.acetheeldritchking.discerning_the_eldritch.networking.devour.SetSyncDevourStacksPacket;
import net.acetheeldritchking.discerning_the_eldritch.registries.*;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEServerConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.UnaryOperator;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.*;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void onPlayerLogInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        Entity entity = event.getEntity();

        // For syncing packets on log in
        if (entity instanceof ServerPlayer player)
        {
            PacketDistributor.sendToPlayer(player, new GetSyncDevourStacksPacket(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogOutEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        Entity entity = event.getEntity();

        // For syncing packets on log out
        if (entity instanceof ServerPlayer player)
        {
            PacketDistributor.sendToPlayer(player, new GetSyncDevourStacksPacket(player));
        }
    }

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
        if (DTEServerConfig.enableInsanitySystem)
        {
            //System.out.println("System enabled?");

            entity.getData(INSANITY_METER);
            entity.getData(IS_INSANE);

            if (spell.getSchoolType() == SchoolRegistry.ELDRITCH.get())
            {
                //System.out.println("Is Eldritch?");

                if (entity.getData(INSANITY_METER) <= (DTEServerConfig.maxInsanityValue - 1))
                {
                    //System.out.println("Increment?");
                    entity.setData(INSANITY_METER, entity.getData(INSANITY_METER) + 1);

                    DiscerningTheEldritch.LOGGER.debug("Meter: " + entity.getData(INSANITY_METER));
                    DiscerningTheEldritch.LOGGER.debug("Max: " + DTEServerConfig.maxInsanityValue);

                    if (entity.getData(INSANITY_METER) == DTEServerConfig.maxInsanityValue)
                    {
                        // We're doing new logic now, having this in a variable instead
                        entity.setData(IS_INSANE, true);

                        if (entity instanceof ServerPlayer player)
                        {
                            if (player.getRandom().nextFloat() * 100.0F < 32.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_1")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 32.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_2")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 32.0F)
                            {
                                player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.insanity_warning_3")
                                        .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                            }
                            if (player.getRandom().nextFloat() * 100.0F < 4.0F)
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
        entity.getData(IS_INSANE);

        // Do this every two seconds
        // Handling this with a bool now
        if (entity.getData(IS_INSANE) == true && entity.tickCount % 100 == 0)
        {
            entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 110, 9, false, false, false));
        }
    }

    @SubscribeEvent
    public static void livingDamageEventPost(LivingDamageEvent.Post event)
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
                        target.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.BLOOD_ROT_EFFECT, 60, 0, true, true, true));
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
                    livingTarget.setTicksFrozen(5*20);
                }
            }
            if (mainhandItem.getItem() instanceof TheSnowgraveItem)
            {
                if (target instanceof LivingEntity livingTarget)
                {
                    livingTarget.setTicksFrozen(15*20);
                }
            }

            // Mend Flesh
            if (livingEntity.hasEffect(DTEPotionEffectRegistry.MEND_FLESH_EFFECT) && DTEServerConfig.mendFleshLifesteal)
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

            // Awakened Cataclysm
            if (mainhandItem.getItem() instanceof CataclysmBladeAwakenedItem && (!(livingEntity instanceof Player player) || !player.getCooldowns().isOnCooldown(ItemRegistries.CATACLYSM_AWAKENED.get())))
            {
                // Zealous - Entities with over 50% of their health are inflicted with Malignant Burn, summoning smaller blades to slice them on hit
                final float MAX_HEALTH = target.getMaxHealth();
                float baseHealth = target.getHealth();
                double percent = (baseHealth/MAX_HEALTH) * 100;

                DiscerningTheEldritch.LOGGER.debug("Max HP: " + MAX_HEALTH);
                DiscerningTheEldritch.LOGGER.debug("Base HP: " + baseHealth);
                DiscerningTheEldritch.LOGGER.debug("Percent: " + percent);

                if (percent > 50)
                {
                    MagicManager.spawnParticles(target.level(), new BlastwaveParticleOptions(DTESchoolRegistry.RITUAL.get().getTargetingColor(), 1.5f), target.getX(), target.getY() + 0.165F, target.getZ(), 1, 0, 0, 0, 0, true);

                    for (int i = 0; i < 5; i++)
                    {
                        CataclysmBladeSmallProjectile cataclysmSmallBlade = new CataclysmBladeSmallProjectile(livingEntity.level(), livingEntity);

                        Vec3 origin = target.getEyePosition().add(target.getForward().normalize().scale(1.2F)).subtract(0, 0.15,0);
                        // Apparently adding it causes it to spawn underground... No clue so we're just subtracting it
                        cataclysmSmallBlade.setPos(origin.subtract(0, cataclysmSmallBlade.getBbHeight() - 1.5, 0));
                        Vec3 vec3 = target.getForward().add(0, 0.05, 0).normalize();
                        cataclysmSmallBlade.shoot(vec3.scale(0.5F), 0.4F);
                        cataclysmSmallBlade.setDamage(2);
                        cataclysmSmallBlade.setHomingTarget(target);

                        livingEntity.level().addFreshEntity(cataclysmSmallBlade);
                    }

                    if (target instanceof LivingEntity livingTarget)
                    {
                        livingTarget.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.MALIGNANT_BURN_EFFECT, 3*20, 0, true, true, true));
                    }

                    if (livingEntity instanceof Player player)
                    {
                        player.getCooldowns().addCooldown(ItemRegistries.CATACLYSM_AWAKENED.get(), CataclysmBladeAwakenedItem.COOLDOWN);
                    }
                }
            }

            // Awakened Devourer
            if (mainhandItem.getItem() instanceof DevourerAxeAwakenedItem && (!(livingEntity instanceof Player player) || !player.getCooldowns().isOnCooldown(ItemRegistries.DEVOURER_AWAKENED.get())))
            {
                // Ravenous - Entities on death are consumed. After consuming 5 entities, spew their innards on the next attack

                if (livingEntity.getData(DEVOURED_ENTITIES) == 5)
                {
                    // spawn AoE here
                    GoreBileAoE goreBileAoE = new GoreBileAoE(livingEntity.level());
                    goreBileAoE.setOwner(livingEntity);
                    goreBileAoE.moveTo(target.position());
                    goreBileAoE.setDamage(5);
                    goreBileAoE.setRadius(4);
                    goreBileAoE.setDuration(5*20);
                    goreBileAoE.setCircular();
                    livingEntity.level().addFreshEntity(goreBileAoE);

                    //target.playSound(DTESoundRegistry.DEVOURER_WRETCH.get(), 1, 1);
                    livingEntity.level().playLocalSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), DTESoundRegistry.DEVOURER_WRETCH.get(), SoundSource.PLAYERS, 1, 1, false);

                    // Reset attachment
                    //livingEntity.setData(DEVOURED_ENTITIES, 0);

                    // Only go on CD if we reach max stacks
                    if (livingEntity instanceof Player player)
                    {
                        //livingEntity.level().playSound(player, livingEntity.blockPosition(), DTESoundRegistry.DEVOURER_WRETCH.get(), SoundSource.PLAYERS);

                        PacketDistributor.sendToPlayer((ServerPlayer) player, new ResetSyncDevourStacksPacket(player));
                        DTEAttachmentSync.resetDevour(livingEntity);
                        player.getCooldowns().addCooldown(ItemRegistries.DEVOURER_AWAKENED.get(), DevourerAxeAwakenedItem.COOLDOWN);
                    } else
                    {
                        livingEntity.setData(DEVOURED_ENTITIES, 0);
                    }
                }
            }

            // Soul Fire Scythe
            if (mainhandItem.getItem() instanceof SoulFireScytheItem)
            {
                // Soul Blazed - After killing 5 entities, your attacks inflict soul fire, and your weapon's special abilities are furthered

                Integer soulFireStacks = mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);
                DiscerningTheEldritch.LOGGER.debug("Start of Soul Fire Scythe? " + soulFireStacks);
                assert soulFireStacks != null;

                if (soulFireStacks >= 5)
                {
                    // Burn them all
                    DiscerningTheEldritch.LOGGER.debug("Are we coming into this?: " + soulFireStacks);
                    target.setRemainingFireTicks(10);

                    FireManager.affect(target, FireManager.SOUL_FIRE_TYPE, Fire::getInFire);
                    FireManager.setOnFire(target, 10, FireManager.SOUL_FIRE_TYPE);

                    // Only go on CD if we reach max stacks
                    /*if (livingEntity instanceof Player player)
                    {
                        player.getCooldowns().addCooldown(ItemRegistries.SOUL_FIRE_SCYTHE.get(), SoulFireScytheItem.COOLDOWN);
                    }

                    mainhandItem.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, 0);*/
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event)
    {
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof LivingEntity livingAttacker)
        {
            ItemStack mainhandItem = livingAttacker.getMainHandItem();

            // Devourer
            if (mainhandItem.getItem() instanceof DevourerAxeAwakenedItem)
            {
                attacker.getData(DEVOURED_ENTITIES);
                //attacker.setData(DEVOURED_ENTITIES, attacker.getData(DEVOURED_ENTITIES) + 1);
                if (livingAttacker instanceof Player player && (target != player))
                {
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new SetSyncDevourStacksPacket(player));
                    DTEAttachmentSync.setDevour(1, player);

                    DiscerningTheEldritch.LOGGER.debug("Devour stacks for player: " + DTEAttachmentSync.getDevour(player));
                } else
                {
                    attacker.setData(DEVOURED_ENTITIES, attacker.getData(DEVOURED_ENTITIES) + 1);

                    DiscerningTheEldritch.LOGGER.debug("Devour stacks for mob: " + attacker.getData(DEVOURED_ENTITIES));
                }
            }

            // Soul Fire Scythe
            if (mainhandItem.getItem() instanceof SoulFireScytheItem && (target != livingAttacker))
            {
                Integer soulFireStacks = mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);

                if (soulFireStacks != null)
                {
                    mainhandItem.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, soulFireStacks + 1);
                    DiscerningTheEldritch.LOGGER.debug("Increment Soul Fire Stacks for player: " + soulFireStacks);
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

        // Awakened Mourning Star
        if (attacker instanceof LivingEntity livingAttacker)
        {
            ItemStack mainhandItem = livingAttacker.getMainHandItem();

            if (mainhandItem.getItem() instanceof MourningStarMaceAwakenedItem && (!(livingAttacker instanceof Player player) || !player.getCooldowns().isOnCooldown(ItemRegistries.MOURNING_STAR_AWAKENED.get())))
            {
                //DiscerningTheEldritch.LOGGER.debug("Is holding");
                // Judgement - Entities with less than 50% of their health have a 50% chance of taking mortal damage or being cursed

                final float MAX_HEALTH = target.getMaxHealth();
                float baseHealth = target.getHealth();
                double percent = (baseHealth/MAX_HEALTH) * 100;

                //DiscerningTheEldritch.LOGGER.debug("Max HP: " + MAX_HEALTH);
                //DiscerningTheEldritch.LOGGER.debug("Base HP: " + baseHealth);
                //DiscerningTheEldritch.LOGGER.debug("Percent: " + percent);

                if (percent < 50)
                {
                    //DiscerningTheEldritch.LOGGER.debug("Is less than 0.5");
                    // Handling a 50% chance in a stupid way
                    if (Utils.random.nextFloat() <= 0.5)
                    {
                        //DiscerningTheEldritch.LOGGER.debug("Instakill");

                        if (livingAttacker instanceof ServerPlayer serverPlayer)
                        {
                            // display a message to the player
                            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.judgement_decree_1")
                                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                        }

                        // Insta-kill w/o insta-killing
                        float baseDamage = event.getOriginalAmount();
                        float newDamage = baseDamage * 5.5F;

                        event.setAmount(newDamage);

                        //DiscerningTheEldritch.LOGGER.debug("Post Max HP: " + MAX_HEALTH);
                        //DiscerningTheEldritch.LOGGER.debug("Post Base HP: " + baseHealth);
                        //DiscerningTheEldritch.LOGGER.debug("Post Percent: " + percent);
                    } else {
                        //DiscerningTheEldritch.LOGGER.debug("Effect");

                        if (livingAttacker instanceof ServerPlayer serverPlayer)
                        {
                            // display a message to the player
                            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.judgement_decree_2")
                                    .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))));
                        }

                        // Curse of Ra
                        if (target instanceof LivingEntity livingTarget)
                        {
                            livingTarget.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.ACCURSED_EFFECT, 5*20, 0, true, true, true));
                        }
                    }

                    if (livingAttacker instanceof Player player)
                    {
                        player.getCooldowns().addCooldown(ItemRegistries.MOURNING_STAR_AWAKENED.get(), MourningStarMaceAwakenedItem.COOLDOWN);
                    }
                }
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
        if (attacker.hasEffect(DTEPotionEffectRegistry.MEND_FLESH_EFFECT) && DTEServerConfig.mendFleshEXPGain)
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
    public static void livingDamageEventPre(LivingDamageEvent.Pre event)
    {
        var entity = event.getEntity();
        var source = event.getSource();
        var attacker = event.getSource().getEntity();

        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        {
            if (entity instanceof LivingEntity livingEntity)
            {
                //Ascended One
                if (livingEntity instanceof AscendedOneBoss ascendedOneBoss && DTEServerConfig.enableAscendedOneDamageCap)
                {
                    float baseDamage = event.getOriginalDamage();
                    float newDamage = ASUtils.basicDamageCap(baseDamage, 0, DTEServerConfig.ascendedOneDamageCap);
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

        if (entity != null)
        {
            if (entity instanceof Player player)
            {
                // Diary of Decay ability
                if (ASUtils.hasCurio(player, ItemRegistries.DIARY_OF_DECAY.get()) && (!player.getCooldowns().isOnCooldown(ItemRegistries.DIARY_OF_DECAY.get())))
                {
                    if (attacker instanceof LivingEntity livingAttacker)
                    {
                        livingAttacker.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.BLOOD_ROT_EFFECT, 100, 0, true, true, true));

                        player.getCooldowns().addCooldown(ItemRegistries.DIARY_OF_DECAY.get(), DiaryOfDecaySpellbook.COOLDOWN);
                    }
                }
            }
        }

        if (attacker != null)
        {
            if (attacker instanceof Player player)
            {
                // Stormweaver's Spell Tome ability
                if (ASUtils.hasCurio(player, ItemRegistries.TEMPESTUOUS_TOME.get()) && (!player.getCooldowns().isOnCooldown(ItemRegistries.TEMPESTUOUS_TOME.get())))
                {
                    if (event.getSource().is(ISSDamageTypes.LIGHTNING_MAGIC))
                    {
                        ChainLightning chainLightning = new ChainLightning(player.level(), player, entity);
                        chainLightning.setDamage(8);
                        chainLightning.range = 10;
                        chainLightning.maxConnections = 7;
                        player.level().addFreshEntity(chainLightning);
                    }

                    player.getCooldowns().addCooldown(ItemRegistries.TEMPESTUOUS_TOME.get(), StormWeaverTomeSpellbook.COOLDOWN);
                }
            }
        }
    }
}
