package net.acetheeldritchking.discerning_the_eldritch.entity.spells.voidsplitter;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoidsplitterProjectile extends AbstractMagicProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final List<Entity> victims = new ArrayList<>();
    private int hitsPerTick;
    private int timer;
    private int windupTimer = 25;
    private static final EntityDataAccessor<Integer> DATA_TIMER = SynchedEntityData.defineId(VoidsplitterProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_DELAY = SynchedEntityData.defineId(VoidsplitterProjectile.class, EntityDataSerializers.INT);

    public VoidsplitterProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        super.setNoGravity(true);
        this.setPierceLevel(-1);
    }

    public VoidsplitterProjectile(Level pLevel, LivingEntity pShooter) {
        super(DTEEntityRegistry.VOIDSPLITTER_PROJECTILE.get(), pLevel);
        this.setOwner(pShooter);
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 3; i++)
        {
            double speed = 0.05F;
            double dx = Math.random() * 2 * speed - speed;
            double dy = Math.random() * 2 * speed - speed;
            double dz = Math.random() * 2 * speed - speed;

            double radius = 4;

            Vec3 upAdjust = this.position().add(new Vec3(Math.sin(Math.toRadians(getXRot() + 180)), 0, Math.cos(Math.toRadians(getXRot() + 180))).scale(radius));
            Vec3 downAdjust = this.position().add(new Vec3(Math.sin(Math.toRadians(getXRot() - 180)), 0, Math.cos(Math.toRadians(getXRot() - 180))).scale(radius));

            // Left
            level().addParticle(ParticleHelper.UNSTABLE_ENDER, upAdjust.x, upAdjust.y, upAdjust.z, dx, dy, dz);

            // Right
            level().addParticle(ParticleHelper.UNSTABLE_ENDER, downAdjust.x, downAdjust.y, downAdjust.z, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {

    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.ECHOING_STRIKE);
    }

    @Override
    public void tick() {
        super.tick();
        hitsPerTick = 0;

        if (tickCount > getDelay())
        {
            if (!level().isClientSide)
            {
                HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

                if (hitresult.getType() == HitResult.Type.ENTITY)
                {
                    onHitEntity((EntityHitResult) hitresult);
                }
            }
        }
    }

    @Override
    protected void rotateWithMotion() {
        return;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();

        if (!victims.contains(entity))
        {
            DamageSources.applyDamage(entity, damage, SpellRegistries.VOID_SPLITTER.get().getDamageSource(this, getOwner()));
            victims.add(entity);
        }

        if (getPierceLevel() != 0)
        {
            if (hitsPerTick++ < 5)
            {
                HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
                if (hitResult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitResult))
                {
                    onHit(hitResult);
                }
            }
            pierceOrDiscard();
        } else
        {
            discard();
        }
    }

    BlockPos lastHitBlock;

    @Override
    protected void onHit(HitResult hitresult) {
        if (!level().isClientSide)
        {
            var blockPos = BlockPos.containing(hitresult.getLocation());
            if (hitresult.getType() == HitResult.Type.BLOCK && !blockPos.equals(lastHitBlock))
            {
                lastHitBlock = blockPos;
            } else if (hitresult.getType() == HitResult.Type.ENTITY) {
                level().playSound(null, BlockPos.containing(position()), SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 2, .65f);
            }
        }

        super.onHit(hitresult);
    }

    @Override
    protected boolean shouldPierceShields() {
        return true;
    }

    // Geckolib & Animations
    private final AnimationController<VoidsplitterProjectile> animationController = new AnimationController<>(this, "controller", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
    }

    private PlayState predicate(AnimationState<VoidsplitterProjectile> event)
    {
        if (tickCount < 20)
        {
            event.getController().setAnimation(RawAnimation.begin().then("spawn", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (tickCount > 20 && tickCount < 100)
        {
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else if (tickCount > 100)
        {
            event.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // NBT
    public int getTimer()
    {
        return entityData.get(DATA_TIMER);
    }

    public void setTimer(int timer)
    {
        entityData.set(DATA_TIMER, timer);
    }

    public int getDelay()
    {
        return entityData.get(DATA_DELAY);
    }

    public void setDelay(int delay)
    {
        entityData.set(DATA_DELAY, delay);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_TIMER, -1);
        pBuilder.define(DATA_DELAY, 20);
    }
}
