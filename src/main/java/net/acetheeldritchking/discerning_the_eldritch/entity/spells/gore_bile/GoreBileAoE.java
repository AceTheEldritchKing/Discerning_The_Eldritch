package net.acetheeldritchking.discerning_the_eldritch.entity.spells.gore_bile;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDamageTypes;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class GoreBileAoE extends AoeEntity {
    public static DamageSource DAMAGE_SOURCE;

    public GoreBileAoE(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GoreBileAoE(Level level)
    {
        this(DTEEntityRegistry.GORE_BILE_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (DAMAGE_SOURCE == null)
        {
            DAMAGE_SOURCE = new DamageSource(DamageSources.getHolderFromResource(target, DTEDamageTypes.GORE_BILE), this, getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(DAMAGE_SOURCE, getDamage());
        //target.addEffect(new MobEffectInstance(ModEffect.EFFECTBLAZING_BRAND, 100, 0));
    }

    @Override
    public float getParticleCount() {
        return 0.2f * getRadius();
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(DTEParticleHelper.MALIGNANT_SOUL);
    }
}
