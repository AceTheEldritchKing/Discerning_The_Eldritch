package net.acetheeldritchking.discerning_the_eldritch.entity.spells.mourning_star;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.gore_bile.GoreBileAoE;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.ritual_burn.RitualBurnAoE;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.*;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class MourningStarProjectile extends AbstractMagicProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MourningStarProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public MourningStarProjectile(Level pLevel, LivingEntity pShooter) {
        super(DTEEntityRegistry.MOURNING_STAR_PROJECTILE.get(), pLevel);
        this.setOwner(pShooter);
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
        return 0.9F;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(DTESoundRegistry.SOUL_SLAM);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level().isClientSide)
        {
            var target = pResult.getEntity();
            var owner = getOwner();

            DamageSources.applyDamage(target, damage, SpellRegistries.LIBRAS_JUDGEMENT.get().getDamageSource(this, owner));

            if (target instanceof LivingEntity livingEntity)
            {
                livingEntity.addEffect(new MobEffectInstance(DTEPotionEffectRegistry.ACCURSED_EFFECT, 5*20, 0, true, true, true));

                //DiscerningTheEldritch.LOGGER.debug("Effect: " + livingEntity.hasEffect(DTEPotionEffectRegistry.MALIGNANT_BURN_EFFECT));
            }

            if (target instanceof Player playerTarget)
            {
                // Disable shield if blocking
                playerTarget.disableShield();
            }

            target.invulnerableTime = 0;

            discard();
        }
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        createAoEField(hitresult.getLocation());

        EarthquakeAoe aoe = new EarthquakeAoe(this.level());
        aoe.moveTo(this.position());
        aoe.setOwner(this);
        aoe.setCircular();
        aoe.setRadius(10);
        aoe.setDuration(20);
        aoe.setDamage(0F);
        aoe.setSlownessAmplifier(0);

        this.level().addFreshEntity(aoe);

        discard();
    }

    public void createAoEField(Vec3 location)
    {
        if (!this.level().isClientSide)
        {
            RitualBurnAoE aoE = new RitualBurnAoE(this.level());
            aoE.setOwner(getOwner());
            aoE.setDuration(100);
            aoE.setDamage(0.5F);
            aoE.setRadius(3.0F);
            aoE.setCircular();
            aoE.moveTo(Utils.moveToRelativeGroundLevel(this.level(), location, 4));
            this.level().addFreshEntity(aoE);
        }
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
