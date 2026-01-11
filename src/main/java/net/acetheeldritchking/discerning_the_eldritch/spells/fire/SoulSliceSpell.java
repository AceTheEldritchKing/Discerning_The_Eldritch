package net.acetheeldritchking.discerning_the_eldritch.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import it.crystalnest.prometheus.api.FireManager;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.SoulFireSlashParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SoulSliceSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "soul_slice");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.soul_slice_spending")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(25)
            .build();

    public SoulSliceSpell()
    {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 35;
        this.spellPowerPerLevel = 1;
        this.castTime = 10;
        this.baseManaCost = 75;
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
        return CastType.LONG;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return false;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean allowCrafting() {
        return false;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.FLAMING_STRIKE_UPSWING.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.FLAMING_STRIKE_SWING.get());
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return getCastTime(spellLevel);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return ASSpellAnimations.ANIMATION_RIGHT_HORIZONTAL_SWORD_SLASH;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 3.25F;
        float distance = 1.9F;
        Vec3 forward = entity.getForward();
        Vec3 hitLoc = entity.position().add(0, entity.getBbHeight() * 0.3F, 0).add(forward.scale(distance));

        var entities = level.getEntities(entity, AABB.ofSize(hitLoc, radius * 2, radius, radius * 2));
        var damageSource = this.getDamageSource(entity);

        for (Entity target : entities)
        {
            if (target instanceof LivingEntity livingTarget && livingTarget.isAlive() && entity.isPickable() && livingTarget.position().subtract(entity.getEyePosition()).dot(forward) >= 0 && entity.distanceToSqr(livingTarget) < radius * radius && Utils.hasLineOfSight(level, entity.getEyePosition(), livingTarget.getBoundingBox().getCenter(), true))
            {
                Vec3 offsetVec = target.getBoundingBox().getCenter().subtract(entity.getEyePosition());

                if (offsetVec.dot(forward) >= 0)
                {
                    ItemStack mainhandItem = entity.getMainHandItem();
                    Integer soulFireStacks = mainhandItem.get(DTEDataComponentRegistry.SOUL_FIRE_STACKS);

                    if (soulFireStacks != null && soulFireStacks >= 2)
                    {
                        if (DamageSources.applyDamage(target, (getDamage(spellLevel, entity) + 2), damageSource))
                        {
                            FireManager.setOnFire(target, 10, FireManager.SOUL_FIRE_TYPE);
                            EnchantmentHelper.doPostAttackEffects((ServerLevel) level, target, damageSource);
                            mainhandItem.set(DTEDataComponentRegistry.SOUL_FIRE_STACKS, soulFireStacks - 2);
                        }
                    } else
                    {
                        if (DamageSources.applyDamage(target, getDamage(spellLevel, entity), damageSource))
                        {
                            FireManager.setOnFire(target, 10, FireManager.SOUL_FIRE_TYPE);
                            EnchantmentHelper.doPostAttackEffects((ServerLevel) level, target, damageSource);
                        }
                    }
                }
            }
        }

        boolean isMirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);
        MagicManager.spawnParticles(level, new SoulFireSlashParticleOptions((float) forward.x, (float) forward.y, (float) forward.z, isMirrored, false, 1F), hitLoc.x, hitLoc.y, hitLoc.z, 1, 0, 0, 0, 0, true);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTicks(0).setLifestealPercent(0.10F);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return (float) ((getSpellPower(spellLevel, caster)) + getWeaponDamage(caster));
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
}
