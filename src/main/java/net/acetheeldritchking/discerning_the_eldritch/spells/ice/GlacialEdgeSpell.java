package net.acetheeldritchking.discerning_the_eldritch.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge.GlacialEdge;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class GlacialEdgeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "glacial_edge");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", getDamage(spellLevel, caster)),
                Component.translatable("ui.discerning_the_eldritch.frozen_weapon_bonus")
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.ICE_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(25)
            .build();

    public GlacialEdgeSpell()
    {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 15;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 55;
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
        return SpellAnimations.SLASH_ANIMATION;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(DTESoundRegistry.ESOTERIC_EDGE_SLASH.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        GlacialEdge glacialEdge = new GlacialEdge(level, entity);
        glacialEdge.setPos(entity.position().add(0, entity.getEyeHeight() - glacialEdge.getBoundingBox().getYsize() * .5f, 0));
        glacialEdge.shootFromRotation(entity, entity.getXRot(), entity.getYHeadRot(), 0, glacialEdge.getSpeed(), 1);

        if (entity.getMainHandItem().is(DTETags.FROZEN_WEAPONS))
        {
            glacialEdge.setDamage(getDamage(spellLevel, entity) * 1.5F);
        } else
        {
            glacialEdge.setDamage(getDamage(spellLevel, entity));
        }

        //System.out.println("Damage: " + esotericEdge.getDamage());

        level.addFreshEntity(glacialEdge);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return (float) ((getSpellPower(spellLevel, caster) * 0.25));
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFreezeTicks(80);
    }
}
