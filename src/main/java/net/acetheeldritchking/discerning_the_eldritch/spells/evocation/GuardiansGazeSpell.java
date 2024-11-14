package net.acetheeldritchking.discerning_the_eldritch.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

@AutoSpellConfig
public class GuardiansGazeSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "guardians_gaze");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(10)
            .build();

    @Override
    public boolean canBeCraftedBy(Player player) {
        return false;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    public GuardiansGazeSpell()
    {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
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
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.RAY_OF_FROST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var hitResult = Utils.raycastForEntity(level, entity, 30, true, 0.15F);
        RayOfFrostVisualEntity ray = new RayOfFrostVisualEntity(level, entity.getEyePosition(), hitResult.getLocation(), entity);
        level.addFreshEntity(ray);

        if (hitResult.getType() == HitResult.Type.ENTITY)
        {
            Entity target = ((EntityHitResult) hitResult).getEntity();
            DamageSources.applyDamage(target, getDamage(spellLevel, entity), SpellRegistries.GUARDIANS_GAZE.get().getDamageSource(entity));
            if (target instanceof LivingEntity livingTarget)
            {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 1, true, true, true));
                MagicManager.spawnParticles(level, ParticleTypes.BUBBLE, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 10, 0, 0, 0, 0.1, true);
            }
            else if (hitResult.getType() == HitResult.Type.BLOCK)
            {
                MagicManager.spawnParticles(level, ParticleTypes.BUBBLE, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 10, 0, 0, 0, 0.1, false);
            }
            MagicManager.spawnParticles(level, ParticleTypes.BUBBLE, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 10, 0, 0, 0, 0.1, false);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 2F;
    }
}
