package net.acetheeldritchking.discerning_the_eldritch.entity.spells.rift_walker;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;

public class UnstableRiftEntity extends AoeEntity {
    private Entity toTrack;
    public final int delay = 20;

    public UnstableRiftEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
    }

    public UnstableRiftEntity(Level level, LivingEntity owner, float damage, float radius) {
        this(DTEEntityRegistry.UNSTABLE_RIFT.get(), level);
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
            this.playSound(DTESoundRegistry.RIFT_WALKER_CAST.get(), 1, Utils.random.nextIntBetweenInclusive(8, 12) * .1f);
            if (!level().isClientSide)
            {
                // Colors - Thanks Google
                int r = 184;
                int g = 11;
                int b = 106;

                float rf = r / 255F;
                float gf = g / 255F;
                float bf = b / 255F;

                Vector3f colorRGB = new Vector3f(rf, gf, bf);

                var center = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(level(), DTEParticleHelper.RIFT_SLICE, center.x, center.y, center.z, 10, 0, 0, 0, .18, false);

                MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(colorRGB, this.getRadius() * .55f), center.x, center.y - 0.75, center.z, 1, 0, 0, 0, 0, true);
                MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(colorRGB, this.getRadius() * .9f), center.x, center.y, center.z, 1, 0, 0, 0, 0, true);
                MagicManager.spawnParticles(level(), new BlastwaveParticleOptions(colorRGB, this.getRadius() * .55f), center.x, center.y + 0.75, center.z, 1, 0, 0, 0, 0, true);

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
        if (level().isClientSide && tickCount < delay / 2)
        {
            Vec3 pos = this.getBoundingBox().getCenter();

            for (int i = 0; i < 3; i++)
            {
                Vec3 vec3 = Utils.getRandomVec3(1F);
                vec3 = vec3.multiply(vec3).multiply(Mth.sign(vec3.x), Mth.sign(vec3.y), Mth.sign(vec3.z)).scale(this.getRadius()).add(pos);
                Vec3 motion = position().subtract(vec3).scale(0.125F);
                level().addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y - .5, vec3.z, motion.x, motion.y, motion.z);
            }
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
