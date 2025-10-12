package net.acetheeldritchking.discerning_the_eldritch.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.crystal_carve.CrystalCarveEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry.FROSTBITE_LEVEL;

@AutoSpellConfig
public class CrystallineCarverSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "crystalline_carver");
    private boolean isFinalCast;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)),
                Component.translatable("ui.discerning_the_eldritch.frozen_weapon_bonus"),
                Component.translatable("ui.discerning_the_eldritch.damage_on_last_recast")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(35)
            .build();

    public CrystallineCarverSpell()
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 50;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        if (isFinalCast)
        {
            return CastType.LONG;
        } else
        {
            return CastType.INSTANT;
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        if (isFinalCast)
        {
            return ASSpellAnimations.ANIMATION_RIGHT_HORIZONTAL_SWORD_SLASH;
        } else
        {
            return SpellAnimations.SLASH_ANIMATION;
        }
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        if (isFinalCast)
        {
            return false;
        } else
        {
            return true;
        }
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return getCastTime(spellLevel);
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        if (isFinalCast)
        {
            return Optional.of(DTESoundRegistry.CRYSTALLINE_CARVER_FINISH.get());
        } else
        {
            return Optional.of(DTESoundRegistry.CRYSTALLINE_CARVER_BASE.get());
        }
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return spellLevel;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId()))
        {
            playerMagicData.getPlayerRecasts().addRecast
                    (new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 8*20, castSource, null), playerMagicData);
        }

        float radius = 3.25F;
        float distance = 1.5F;
        Vec3 hitLocation = entity.position().add(0, entity.getBbHeight() * 0.3F, 0).add(entity.getForward().multiply(distance, 0.35F, distance));
        var entities = entity.level().getEntities(entity, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));

        for (Entity target : entities)
        {
            if (entity.isPickable() && entity.distanceToSqr(target) < radius * radius && Utils.hasLineOfSight(level, entity.getEyePosition(), target.getBoundingBox().getCenter(), true) && !(target instanceof ItemEntity))
            {
                if (target instanceof LivingEntity livingEntity)
                {
                    int frostbiteLevel = target.getData(FROSTBITE_LEVEL);

                    DiscerningTheEldritch.LOGGER.debug("Applying chilled");
                    target.setData(FROSTBITE_LEVEL, frostbiteLevel + 1);
                    livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED, 3 * 20, 0, false, false, false));

                    DiscerningTheEldritch.LOGGER.debug("Frostbite: " + target.getData(FROSTBITE_LEVEL));
                }
            }
        }

        boolean mirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);

        CrystalCarveEntity swipe = new CrystalCarveEntity(level, mirrored);
        swipe.moveTo(hitLocation);
        swipe.setYRot(entity.getYRot());
        swipe.setIsFinal(false);
        level.addFreshEntity(swipe);

        isFinalCast = playerMagicData.getPlayerRecasts().getRemainingRecastsForSpell(spellId.toString()) == 2;

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);

        if (recastResult.isSuccess())
        {
            float radius = 2.15F;
            float distance = 1.2F;
            Vec3 hitLocation = serverPlayer.position().add(0, serverPlayer.getBbHeight() * 0.3F, 0).add(serverPlayer.getForward().multiply(distance, 0.35F, distance));
            var entities = serverPlayer.level().getEntities(serverPlayer, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));

            for (Entity target : entities)
            {
                if (serverPlayer.isPickable() && serverPlayer.distanceToSqr(target) < radius * radius && Utils.hasLineOfSight(serverPlayer.level(), serverPlayer.getEyePosition(), target.getBoundingBox().getCenter(), true) && !(target instanceof ItemEntity))
                {
                    float baseDamage = getDamage(recastInstance.getSpellLevel(), serverPlayer);

                    if (target instanceof LivingEntity livingTarget)
                    {
                        if (livingTarget.hasEffect(MobEffectRegistry.CHILLED))
                        {
                            int amplifier = target.getData(FROSTBITE_LEVEL);

                            if (serverPlayer.getMainHandItem().is(DTETags.FROZEN_WEAPONS))
                            {
                                float extraDamage = (baseDamage + amplifier) * 1.5F;

                                if (DamageSources.applyDamage(target, extraDamage, this.getDamageSource(serverPlayer)))
                                {
                                    MagicManager.spawnParticles(serverPlayer.level(), ParticleHelper.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                                    EnchantmentHelper.doPostAttackEffects((ServerLevel) serverPlayer.level(), target, this.getDamageSource(serverPlayer));

                                    DiscerningTheEldritch.LOGGER.debug("Bonus damage (Icy): " + extraDamage);
                                }
                            } else
                            {
                                float extraDamage = baseDamage + amplifier;

                                if (DamageSources.applyDamage(target, extraDamage, this.getDamageSource(serverPlayer)))
                                {
                                    MagicManager.spawnParticles(serverPlayer.level(), ParticleHelper.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                                    EnchantmentHelper.doPostAttackEffects((ServerLevel) serverPlayer.level(), target, this.getDamageSource(serverPlayer));

                                    DiscerningTheEldritch.LOGGER.debug("Bonus damage: " + extraDamage);
                                }
                            }
                        } else
                        {
                            if (serverPlayer.getMainHandItem().is(DTETags.FROZEN_WEAPONS))
                            {
                                if (DamageSources.applyDamage(target, (baseDamage * 1.5F), this.getDamageSource(serverPlayer)))
                                {
                                    MagicManager.spawnParticles(serverPlayer.level(), ParticleHelper.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                                    EnchantmentHelper.doPostAttackEffects((ServerLevel) serverPlayer.level(), target, this.getDamageSource(serverPlayer));

                                    DiscerningTheEldritch.LOGGER.debug("Base damage (in living.hasEffect() (Icy)): " + baseDamage);
                                }
                            } else
                            {
                                if (DamageSources.applyDamage(target, baseDamage, this.getDamageSource(serverPlayer)))
                                {
                                    MagicManager.spawnParticles(serverPlayer.level(), ParticleHelper.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                                    EnchantmentHelper.doPostAttackEffects((ServerLevel) serverPlayer.level(), target, this.getDamageSource(serverPlayer));

                                    DiscerningTheEldritch.LOGGER.debug("Base damage (in living.hasEffect()): " + baseDamage);
                                }
                            }
                        }
                    } else
                    {
                        if (DamageSources.applyDamage(target, baseDamage, this.getDamageSource(serverPlayer)))
                        {
                            MagicManager.spawnParticles(serverPlayer.level(), ParticleHelper.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                            EnchantmentHelper.doPostAttackEffects((ServerLevel) serverPlayer.level(), target, this.getDamageSource(serverPlayer));

                            DiscerningTheEldritch.LOGGER.debug("Base damage (outside): " + baseDamage);
                        }
                    }
                }

                target.setData(FROSTBITE_LEVEL, 0);
            }

            //boolean mirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);

            CrystalCarveEntity swipe = new CrystalCarveEntity(serverPlayer.level(), false);
            swipe.moveTo(hitLocation);
            swipe.setYRot(serverPlayer.getYRot());
            swipe.setIsFinal(true);
            serverPlayer.level().addFreshEntity(swipe);
        }
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        float baseDamage = getSpellPower(spellLevel, caster) * 0.5F;

        return baseDamage + getWeaponDamage(caster);
    }

    private float getWeaponDamage(LivingEntity caster)
    {
        float weaponDamage = Utils.getWeaponDamage(caster);

        return weaponDamage;
    }

    private String getDamageText(int spellLevel, LivingEntity caster)
    {
        if (caster != null)
        {
            float weaponDamage = Utils.getWeaponDamage(caster);
            String plus = "";
            if (weaponDamage > 0)
            {
                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
            }
            String damage = Utils.stringTruncation(getDamage(spellLevel, caster), 1);
            return damage + plus;
        }
        return "" + getSpellPower(spellLevel, caster);
    }

    private MobEffectInstance getEffect(ArrayList<MobEffectInstance> effects, MobEffect mobEffect)
    {
        for (var effect : effects)
        {
            if (effect.getEffect().equals(mobEffect))
            {
                return effect;
            }
        }

        return null;
    }
}
