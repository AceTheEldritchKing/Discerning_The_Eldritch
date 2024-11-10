package net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class EsotericEdge extends AbstractMagicProjectile implements AntiMagicSusceptible {
    public EsotericEdge(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);

        this.setNoGravity(true);
    }

    public EsotericEdge(EntityType<? extends Projectile> entityType, Level level, LivingEntity shooter)
    {
        this(entityType, level);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
    }

    public EsotericEdge(Level level, LivingEntity shooter)
    {
        this(DTEEntityRegistry.ESOTERIC_EDGE.get(), level, shooter);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        setDeltaMovement(x, y, z);
    }

    @Override
    public void trailParticles() {

    }

    @Override
    public void impactParticles(double x, double y, double z) {

    }

    @Override
    public float getSpeed() {
        return 1;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        var target = pResult.getEntity();
        DamageSources.applyDamage(target, damage,
                SpellRegistry.ESOTERIC_EDGE.get().getDamageSource(this, getOwner()));

        // Kills shields & Do effects
        if (target instanceof LivingEntity livingTarget)
        {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
            if (livingTarget instanceof Player player)
            {
                player.disableShield();
            }
        }
        if (target instanceof ShieldPart || target instanceof AbstractShieldEntity)
        {
            target.discard();
        }
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.discard();
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        //
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        //
    }
}
