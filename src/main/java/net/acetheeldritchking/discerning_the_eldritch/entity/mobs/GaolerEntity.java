package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class GaolerEntity extends Monster implements IMagicSummon, GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int riseAnimationTime = 40;
    private static final EntityDataAccessor<Boolean> DATA_IS_PLAYING_RISE_ANIM = SynchedEntityData.defineId(GaolerEntity.class, EntityDataSerializers.BOOLEAN);

    public GaolerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
    }

    public GaolerEntity(Level level, LivingEntity owner, boolean playAnimation) {
        this(DTEEntityRegistry.GAOLER_ENTITY.get(), level);
        setSummoner(owner);
        if (playAnimation)
        {
            triggerRiseAnimation();
        }
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 10, 2, false, 50));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());

    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.5)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.FOLLOW_RANGE, 45.0)
                .add(Attributes.MOVEMENT_SPEED, .25);
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner)
    {
        if (owner != null)
        {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this)
        {
            return true;
        }
        else if (entityIn == getSummoner())
        {
            return true;
        }
        else if (getSummoner() != null && !entityIn.isAlliedTo(getSummoner().getTeam()))
        {
            return false;
        }
        else
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WARDEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WARDEN_STEP, 10.0F, 1.0F);
    }

    // Attacks and Death
    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this, DTEPotionEffectRegistry.GAOLER_TIMER);
        super.onRemovedFromLevel();
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide)
        {
            MagicManager.spawnParticles(this.level(), ParticleTypes.SCULK_SOUL,
                    getX(), getY(), getZ(),
                    25, 0.4, 0.8, 0.4, 0.03, false);
            discard();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.level().broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 10.0F, this.getVoicePitch());
        SonicBoom.setCooldown(this, 40);

        return Utils.doMeleeAttack(this, entity, SpellRegistries.CONJURE_GAOLER.get().getDamageSource(this, getSummoner()));
    }

    // Geckolib & Animations
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState event)
    {
        if (!isPlayingRiseAnimation())
        {
            if (this.swinging)
            {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("attacking"));
            }
            else if (event.isMoving())
            {
                event.getController().setAnimation(RawAnimation.begin().then("walking", Animation.LoopType.LOOP));
            }
            else if (!event.isMoving())
            {
                event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }
        }
        else
        {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        return this.tickCount;
    }

    private void clientDiggingParticles(LivingEntity livingEntity)
    {
        RandomSource randomSource = this.getRandom();
        BlockState blockState = this.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE)
        {
            for (int i = 0; i < 30; ++i)
            {
                double x = livingEntity.getX() + Mth.randomBetween(randomSource, -0.7F, 0.7F);
                double y = livingEntity.getY();
                double z = livingEntity.getZ() + Mth.randomBetween(randomSource, -0.7F, 0.7F);
                livingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, y , z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean isPlayingRiseAnimation()
    {
        return entityData.get(DATA_IS_PLAYING_RISE_ANIM);
    }

    public void triggerRiseAnimation()
    {
        entityData.set(DATA_IS_PLAYING_RISE_ANIM, true);
    }

    @Override
    public void tick() {
        if (isPlayingRiseAnimation())
        {
            if (this.level().isClientSide)
            {
                clientDiggingParticles(this);
            }
            if (--riseAnimationTime < 0)
            {
                entityData.set(DATA_IS_PLAYING_RISE_ANIM, false);
                this.setXRot(0);
                this.setOldPosAndRot();
            }
        }
        else {
            super.tick();
        }
    }

    // NBT
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.summonerUUID = OwnerHelper.deserializeOwner(pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        OwnerHelper.serializeOwner(pCompound, summonerUUID);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_PLAYING_RISE_ANIM, false);
    }
}
