package net.acetheeldritchking.discerning_the_eldritch.entity.spells.rift_walker;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UnstableRiftEntity extends AoeEntity {
    private Entity toTrack;
    public final int delay = 20;

    public UnstableRiftEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
    }

    public UnstableRiftEntity(Level level, LivingEntity owner, float damage, float radius) {
        this(EntityRegistry.ECHOING_STRIKE.get(), level);
        setOwner(owner);
        this.setRadius(radius);
        this.setDamage(damage);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        return;
    }

    public void setTracking(Entity entity)
    {
        this.toTrack = entity;
    }

    @Override
    public void tick() {
        if (toTrack != null && tickCount < delay / 2)
        {
            this.setPos(toTrack.position());
        } else if (tickCount == delay) {
            this.playSound(SoundRegistry.FORCE_IMPACT.get(), 1, Utils.random.nextIntBetweenInclusive(8, 12) * .1f);
            if (!level().isClientSide)
            {
                var center = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(SpellRegistries.RIFT_WALKER.get().getSchoolType().getTargetingColor(), this.getRadius() * .9f), center.x, center.y, center.z, 1, 0, 0, 0, 0, true);

                float explosionRadius = getRadius();
                var explosionRadiusSqr = explosionRadius * explosionRadius;
                var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
                var locCenter = Utils.moveToRelativeGroundLevel(level(), center, 2);
                locCenter = Utils.raycastForBlock(level(), locCenter, locCenter.add(0, 3, 0), ClipContext.Fluid.NONE).getLocation().add(locCenter).scale(0.5F);

                for (Entity entity : entities)
                {
                    double distSqr = entity.distanceToSqr(center);
                    if (distSqr < explosionRadiusSqr && canHitEntity(entity) && Utils.hasLineOfSight(level(), locCenter, entity.getBoundingBox().getCenter(), false));
                    {
                        double p = Mth.clamp((1 - distSqr /explosionRadiusSqr) + 0.4F, 0, 1);
                        float damage = (float) (this.damage * p);
                        DamageSources.applyDamage(entity, damage, SpellRegistries.RIFT_WALKER.get().getDamageSource(this, getOwner()));
                    }
                }
            }
        } else if (tickCount > delay) {
            discard();
        }
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2, this.getRadius() * 2);
    }

    @Override
    public float getParticleCount() {
        return 0;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}
