package net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
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

public class GlacialEdge extends AbstractMagicProjectile implements AntiMagicSusceptible {
    private List<Entity> entities;

    public GlacialEdge(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        entities = new ArrayList<>();
        this.setNoGravity(true);
    }

    public GlacialEdge(EntityType<? extends Projectile> entityType, Level level, LivingEntity shooter)
    {
        this(entityType, level);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
    }

    public GlacialEdge(Level level, LivingEntity shooter)
    {
        this(DTEEntityRegistry.GLACIAL_EDGE.get(), level, shooter);
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
        for (int i = 0; i < 3; i++)
        {
            double speed = 0.05F;
            double dx = Utils.random.nextDouble() * 2 * speed - speed;
            double dy = Utils.random.nextDouble() * 2 * speed - speed;
            double dz = Utils.random.nextDouble() * 2 * speed - speed;

            level().addParticle(Utils.random.nextDouble() < 0.3 ? ParticleHelper.SNOWFLAKE : ParticleTypes.SNOWFLAKE, this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), ParticleHelper.ICY_FOG, x, y, z, 1, 0, 0, 0, 0.1, true);
    }

    @Override
    public float getSpeed() {
        return 1.2F;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.ICE_IMPACT);
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    private void damageEntity(Entity entity)
    {
        if (!entities.contains(entity))
        {
            DamageSources.applyDamage(entity, damage,
                    SpellRegistries.GLACIAL_EDGE.get().getDamageSource(this, getOwner()));

            //Do effects
            if (entity instanceof LivingEntity livingTarget)
            {
                livingTarget.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED, 30, 0));
            }
            if (entity instanceof ShieldPart || entity instanceof AbstractShieldEntity)
            {
                entity.kill();
            }

            entities.add(entity);
        }
    }

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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        //
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        MagicManager.spawnParticles(level(), ParticleHelper.ICY_FOG, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.1, true);
    }
}
