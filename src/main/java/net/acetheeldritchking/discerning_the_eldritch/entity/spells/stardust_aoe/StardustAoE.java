package net.acetheeldritchking.discerning_the_eldritch.entity.spells.stardust_aoe;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class StardustAoE extends AoeEntity {
    public StardustAoE(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StardustAoE(Level level)
    {
        this(DTEEntityRegistry.STARDUST_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        target.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.STARDUSTED_EFFECT, 100, 0));
    }

    @Override
    public float getParticleCount() {
        return 0.3f * getRadius();
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(DTEParticleHelper.STARDUST);
    }
}
