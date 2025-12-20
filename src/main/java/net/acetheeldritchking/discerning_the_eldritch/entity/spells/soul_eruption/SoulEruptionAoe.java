package net.acetheeldritchking.discerning_the_eldritch.entity.spells.soul_eruption;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import it.crystalnest.prometheus.api.FireManager;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SoulEruptionAoe extends AoeEntity {
    public static DamageSource DAMAGE_SOURCE;
    private int waveAnimTime = -1;

    public SoulEruptionAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public SoulEruptionAoe(Level level, float radius) {
        super(DTEEntityRegistry.SOUL_FIRE_ERUPTION_AOE.get(), level);
        this.setRadius(radius);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (DAMAGE_SOURCE == null)
        {
            DAMAGE_SOURCE = SpellRegistries.SOUL_SLICE.get().getDamageSource(getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(DAMAGE_SOURCE, getDamage());
        // Yeah, you're gonna hurt *bad*
        FireManager.setOnFire(target, 25, FireManager.SOUL_FIRE_TYPE);
    }

    @Override
    public void tick() {
        // Basically what Fire Eruption is doing but we make it SOOOOUUULLL FIIIYYAHHHH
        var radius = getRadius();
        var level = level();

        if (waveAnimTime++ < radius)
        {
            if (!level.isClientSide())
            {
                if (waveAnimTime % 2 == 0)
                {
                    float vol = (waveAnimTime + 8) / 16F;
                    this.playSound(SoundRegistry.FIERY_EXPLOSION.get(), vol, Utils.random.nextIntBetweenInclusive(90, 110) * .01f);
                }

                var circMin = (waveAnimTime - 1) * 2 * 3.14F;
                var circMax = (waveAnimTime + 1) * 2 * 3.14F;
                int minBlocks = (int) Mth.clamp(circMin, 0, 60);
                int maxBlocks = (int) Mth.clamp(circMax, 0, 60);
                float anglePerBlockMin = 360F / minBlocks;
                float anglePerBlockMax = 360F / maxBlocks;

                for (int i = 0; i < minBlocks; i++)
                {
                    Vec3 vec3 = new Vec3(
                            waveAnimTime * Mth.cos(anglePerBlockMin * i),
                            0,
                            waveAnimTime * Mth.sin(anglePerBlockMin * i)
                    );

                    BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, position().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1F + random.nextFloat() * 0.2F);
                }

                for (int i = 0; i < maxBlocks; i++)
                {
                    Vec3 vec3 = new Vec3(
                            (waveAnimTime + 1) * Mth.cos(anglePerBlockMax * i),
                            0,
                            (waveAnimTime + 1) * Mth.sin(anglePerBlockMax * i)
                    );

                    BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, position().add(vec3), 4).add(0, 0.1, 0));

                    if (level.getBlockState(blockPos.below()).isFaceSturdy(level, blockPos.below(), Direction.UP))
                    {
                        Utils.createTremorBlockWithState(level, Blocks.SOUL_FIRE.defaultBlockState(), blockPos, 0.1F + random.nextFloat() * 0.2F);
                    }
                }

                List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getInflation().x, this.getInflation().y, this.getInflation().z));

                var r1Sqr = waveAnimTime * waveAnimTime;
                var r2Sqr = (waveAnimTime + 1) * (waveAnimTime + 1);

                for (LivingEntity target : targets)
                {
                    var distSqr = target.distanceToSqr(this);

                    if (canHitEntity(target) && distSqr >= r1Sqr && distSqr <= r2Sqr)
                    {
                        if (canHitTargetForGroundContext(target))
                        {
                            applyEffect(target);
                        }
                    }
                }
            } else
            {
                int particles = (int) ((waveAnimTime + 1) * 2 * 3.14F * 2.5F);
                // Pi was so good they made Pi 2
                float anglePerParticle = Mth.TWO_PI / particles;

                for (int i = 0; i < particles; i++)
                {
                    Vec3 vec3 = new Vec3(
                            Mth.cos(anglePerParticle * i),
                            0,
                            Mth.sin(anglePerParticle * i)
                    );

                    float r = Mth.lerp(Utils.random.nextFloat(), waveAnimTime, waveAnimTime + 1);
                    Vec3 pos = vec3.scale(r).add(Utils.getRandomVec3(0.4)).add(this.position()).add(0, 0.5F, 0);
                    Vec3 motion = vec3.add(Utils.getRandomVec3(0.5)).scale(0.1);
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                    level.addParticle(ParticleTypes.SOUL, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                }
            }
        } else
        {
            this.discard();
        }
    }

    @Override
    public float getParticleCount() {
        return 0;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return !this.level().noCollision(target.getBoundingBox().move(new Vec3(0, -.9999, 0)));
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0, 5, 0);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 3F);
    }
}
