package net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESchoolRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class CataclysmBladeBigProjectile extends AbstractMagicProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int hitsPerTick;

    public CataclysmBladeBigProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public CataclysmBladeBigProjectile(Level pLevel, LivingEntity pShooter) {
        super(DTEEntityRegistry.CATACLYSM_BLADE_BIG.get(), pLevel);
        this.setOwner(pShooter);
    }

    public void shoot(Vec3 rotation, float inaccuracy) {
        var speed = rotation.length();
        Vec3 offset = Utils.getRandomVec3(1).normalize().scale(inaccuracy);
        var motion = rotation.normalize().add(offset).normalize().scale(speed);
        super.shoot(motion);
    }

    @Override
    protected void setRot(float y, float x) {
        this.setXRot(x);
        this.xRotO = x;
        this.setYRot(y);
        this.yRotO = y;
    }

    @Override
    public void tick() {
        Vec3 deltaMovement = getDeltaMovement();
        double distance = deltaMovement.horizontalDistance();

        double x = deltaMovement.x;
        double y = deltaMovement.y;
        double z = deltaMovement.z;

        setYRot((float) (Mth.atan2(x, z) * (180 / Math.PI)));
        setXRot((float) (Mth.atan2(y, distance) * (180 / Math.PI)));
        setXRot(lerpRotation(xRotO, getXRot()));
        setYRot(lerpRotation(yRotO, getYRot()));
        super.tick();

        hitsPerTick = 0;

        // Limiting how much time these guys remain in the world
        if (tickCount >= 60)
        {
            this.discard();
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 3; i++)
        {
            double speed = 0.05;
            double x = Utils.random.nextDouble() * 2 * speed - speed;
            double y = Utils.random.nextDouble() * 2 * speed - speed;
            double z = Utils.random.nextDouble() * 2 * speed - speed;
            this.level().addParticle(DTEParticleHelper.MALIGNANT_FLAME, this.getX(), this.getY(), this.getZ(), x, y, z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level(), new BlastwaveParticleOptions(DTESchoolRegistry.RITUAL.get().getTargetingColor(), 1f), this.getX(), this.getY() + 0.165F, this.getZ(), 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(this.level(), new BlastwaveParticleOptions(DTESchoolRegistry.RITUAL.get().getTargetingColor(), 1.5f), this.getX(), this.getY() + 0.165F, this.getZ(), 1, 0, 0, 0, 0, true);
    }

    @Override
    public float getSpeed() {
        return 1.3F;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level().isClientSide)
        {
            var target = pResult.getEntity();
            var owner = getOwner();

            DamageSources.applyDamage(target, damage, SpellRegistries.ZEALOUS_HARBINGER.get().getDamageSource(this, owner));

            if (target instanceof LivingEntity livingEntity)
            {
                livingEntity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.ACCURSED_EFFECT, 5*20, 0, true, true, true));

                //DiscerningTheEldritch.LOGGER.debug("Effect: " + livingEntity.hasEffect(DTEPotionEffectRegistry.MALIGNANT_BURN_EFFECT));
            }

            target.invulnerableTime = 0;

            if (hitsPerTick++ < 2)
            {
                discard();
                //DiscerningTheEldritch.LOGGER.debug("Bai bai");
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        //super.onHitBlock(result);
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
