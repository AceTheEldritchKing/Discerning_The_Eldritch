package net.acetheeldritchking.discerning_the_eldritch.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.aces_spell_utils.spells.ASSpellAnimations;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_strike.EsotericStrike;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class EsotericStrikeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "esoteric_strike");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.extra_damage"),
                Component.translatable("ui.discerning_the_eldritch.melee_damage")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(1)
            .build();

    public EsotericStrikeSpell()
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 25;
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
        return CastType.INSTANT;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return ASSpellAnimations.ANIMATION_SPELL_PUNCH;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(DTESoundRegistry.ESOTERIC_STRIKE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 2.15F;
        float distance = 1.2F;
        Vec3 hitLocation = entity.position().add(0, entity.getBbHeight() * 0.3F, 0).add(entity.getForward().multiply(distance, 0.35F, distance));
        var entities = entity.level().getEntities(entity, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));

        for (Entity target : entities)
        {
            if (entity.isPickable() && entity.distanceToSqr(target) < radius * radius && Utils.hasLineOfSight(level, entity.getEyePosition(), target.getBoundingBox().getCenter(), true) && !(target instanceof ItemEntity))
            {
                if (DamageSources.applyDamage(target, getDamage(spellLevel, entity), this.getDamageSource(entity)))
                {
                    MagicManager.spawnParticles(level, DTEParticleHelper.ESOTERIC_SPARKS, target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f, .03, false);
                    EnchantmentHelper.doPostAttackEffects((ServerLevel) level, target, this.getDamageSource(entity));
                }
            }
        }

        boolean mirrored = playerMagicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);

        EsotericStrike swipe = new EsotericStrike(level, mirrored);
        swipe.moveTo(hitLocation);
        swipe.setYRot(entity.getYRot());
        level.addFreshEntity(swipe);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        float baseDamage = ASUtils.getDamageForAttributes(this, caster, spellLevel, Attributes.ATTACK_DAMAGE, 0.35F);

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
}
