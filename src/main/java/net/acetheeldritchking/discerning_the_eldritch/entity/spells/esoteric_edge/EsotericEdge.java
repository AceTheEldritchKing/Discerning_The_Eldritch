package net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EsotericEdge extends AbstractMagicProjectile implements AntiMagicSusceptible {
    private List<Entity> entities;

    public EsotericEdge(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);

        entities = new ArrayList<>();
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
    public void travel() {
        this.setPos(this.position().add(this.getDeltaMovement()));
        if (!this.isNoGravity())
        {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x, vec3.y - 0.05000000074505806, vec3.z);
        }
    }

    @Override
    public void tick() {
        if (!level().isClientSide)
        {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hitresult);
            }
            for (Entity entity : level().getEntities(this, this.getBoundingBox()).stream().filter(target -> canHitEntity(target) && !entities.contains(target)).collect(Collectors.toSet())) {
                damageEntity(entity);
            }
        }

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
    }

    @Override
    public void trailParticles() {

    }

    @Override
    public void impactParticles(double x, double y, double z) {

    }

    @Override
    public float getSpeed() {
        return 1.2F;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    /*@Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        var target = pResult.getEntity();

        if (!entities.contains(target))
        {
            DamageSources.applyDamage(target, damage,
                    SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner()));

            // Kills shields & Do effects
            if (target instanceof LivingEntity livingTarget)
            {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
                if (livingTarget instanceof Player player)
                {
                    player.disableShield();
                }
                if (DamageSources.applyDamage(livingTarget, damage, SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner())))
                {
                    EnchantmentHelper.doPostAttackEffects((ServerLevel) this.level(), livingTarget, SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner()));
                }
            }
            if (target instanceof ShieldPart || target instanceof AbstractShieldEntity)
            {
                target.kill();
            }

            entities.add(target);
        }
    }*/

    private void damageEntity(Entity entity)
    {
        if (!entities.contains(entity))
        {
            DamageSources.applyDamage(entity, damage,
                    SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner()));

            // Kills shields & Do effects
            if (entity instanceof LivingEntity livingTarget)
            {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
                if (livingTarget instanceof Player player)
                {
                    player.disableShield();
                }
                if (DamageSources.applyDamage(livingTarget, damage, SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner())))
                {
                    EnchantmentHelper.doPostAttackEffects((ServerLevel) this.level(), livingTarget, SpellRegistries.ESOTERIC_EDGE.get().getDamageSource(this, getOwner()));
                }
            }
            if (entity instanceof ShieldPart || entity instanceof AbstractShieldEntity)
            {
                entity.kill();
            }

            entities.add(entity);
        }
    }

    /*@Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.discard();
    }*/

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return pTarget != getOwner() && super.canHitEntity(pTarget);
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
