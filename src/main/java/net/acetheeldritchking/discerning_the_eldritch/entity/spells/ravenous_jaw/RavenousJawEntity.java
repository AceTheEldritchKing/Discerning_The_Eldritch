package net.acetheeldritchking.discerning_the_eldritch.entity.spells.ravenous_jaw;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class RavenousJawEntity extends AoeEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    LivingEntity target;

    public RavenousJawEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RavenousJawEntity(Level level, LivingEntity owner, LivingEntity target) {
        this(DTEEntityRegistry.RAVENOUS_JAW.get(), level);
        setOwner(owner);
        this.target = target;
    }

    @Override
    public void applyEffect(LivingEntity target) {
        //
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
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    public void tick() {
        if (tickCount < riseAnimTime)
        {
            if (this.target != null)
            {
                setPos(this.target.position());
            }
        } else if (tickCount == riseAnimTime)
        {
            this.playSound(SoundRegistry.DEVOUR_BITE.get(), 2, 0.5F);
        } else if (tickCount == waitAnimTime)
        {
            if (level().isClientSide())
            {
                float y = this.getYRot();
                int countsPerSide = 25;
                for (int i = -countsPerSide; i < countsPerSide; i++)
                {
                    Vec3 motion = new Vec3(0, Math.abs(countsPerSide) - i, countsPerSide * .5f).yRot(y).normalize().multiply(.4f, .8f, .4f);
                    this.level().addParticle(DTEParticleHelper.MALIGNANT_FLAME, getX(), getY() + .5f, getZ(), motion.x, motion.y, motion.z);
                }
            } else
            {
                checkHits();
            }
        } else if (tickCount > dueToFallAnimTime)
        {
            discard();
        }
    }

    // Geckolib
    private final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("rise");
    private final RawAnimation FALL_ANIM = RawAnimation.begin().thenPlay("fall");
    private final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idle");
    private int riseAnimTime = 15;
    private int waitAnimTime = riseAnimTime + 15;
    private int dueToFallAnimTime = waitAnimTime + 30;

    private final AnimationController<RavenousJawEntity> animationController = new AnimationController<>(this, "controller", 0, this::predicate);
    private final AnimationController<RavenousJawEntity> riseAnimationController = new AnimationController<>(this, "rise_controller", 0, this::risePredicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
        controllerRegistrar.add(riseAnimationController);
    }

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState<RavenousJawEntity> event)
    {
        var controller = event.getController();

        if (tickCount > dueToFallAnimTime)
        {
            controller.setAnimation(FALL_ANIM);
        } else if (controller.getAnimationState() == AnimationController.State.STOPPED)
        {
            controller.setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState<RavenousJawEntity> event)
    {
        var controller = event.getController();

        if (tickCount < riseAnimTime)
        {
            controller.setAnimation(RISE_ANIM);
            return PlayState.CONTINUE;
        } else
        {
            return PlayState.STOP;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
