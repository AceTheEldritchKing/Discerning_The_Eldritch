package net.acetheeldritchking.discerning_the_eldritch.entity.spells.ritual_burn;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDamageTypes;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RitualBurnAoE extends AoeEntity {
    public static DamageSource DAMAGE_SOURCE;

    public RitualBurnAoE(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RitualBurnAoE(Level level)
    {
        this(DTEEntityRegistry.RITUAL_BURN_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (DAMAGE_SOURCE == null)
        {
            DAMAGE_SOURCE = SpellRegistries.LIBRAS_JUDGEMENT.get().getDamageSource(getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(DAMAGE_SOURCE, getDamage());
        target.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.MALIGNANT_BURN_EFFECT, 100, 0));
    }

    @Override
    public float getParticleCount() {
        return 0.3f * getRadius();
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(DTEParticleHelper.MALIGNANT_FLAME);
    }
}
