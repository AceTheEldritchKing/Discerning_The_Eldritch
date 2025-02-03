package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.GaolerAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.registries.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;


public class GaolerEntity extends AbstractSpellCastingMob implements IMagicSummon, GeoAnimatable, IAnimatedAttacker {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int riseAnimationTime = 130;
    private static final EntityDataAccessor<Boolean> DATA_IS_PLAYING_RISE_ANIM = SynchedEntityData.defineId(GaolerEntity.class, EntityDataSerializers.BOOLEAN);

    public GaolerEntity(EntityType<? extends AbstractSpellCastingMob> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    public GaolerEntity(Level level, LivingEntity owner, boolean playAnimation) {
        this(DTEEntityRegistry.GAOLER_ENTITY.get(), level);
        setSummoner(owner);
        if (playAnimation)
        {
            triggerRiseAnimation();
        }
    }

    protected LookControl createLookControl()
    {
        return new LookControl(this)
        {
            @Override
            protected float rotateTowards(float from, float to, float maxDelta) {
                return super.rotateTowards(from, to, maxDelta * 2.5F);
            }

            @Override
            protected boolean resetXRotOnTick() {
                return getTarget() == null;
            }
        };
    }

    protected MoveControl createMoveControl()
    {
        return new MoveControl(this)
        {
            @Override
            protected float rotlerp(float sourceAngle, float targetAngle, float maximumChange) {
                double x = this.wantedX - this.mob.getX();
                double z = this.wantedZ - this.mob.getZ();

                if (x * x + z * z < 0.5F)
                {
                    return sourceAngle;
                }
                else
                {
                    return super.rotlerp(sourceAngle, targetAngle, maximumChange * 0.25F);
                }
            }
        };
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GaolerAnimatedWarlockAttackGoal(this, 1.0F, 25, 55, 5f)
                .setMoveset(List.of(
                        new AttackAnimationData(39, "slam_1", 24),
                        new AttackAnimationData(37, "upper_cut", 22)
                ))
                .setComboChance(0.8f)
                .setMeleeAttackInverval(10, 25)
                .setMeleeMovespeedModifier(1.5f)
                .setSingleUseSpell(SpellRegistry.SONIC_BOOM_SPELL.get(), 10, 15, 10, 10)
                .setSpellQuality(15, 15)
        );
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 10, 2, false, 50));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        // Attack mobs on sight
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, true));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());

    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10.5)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.MAX_HEALTH, 350.0)
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
            //return this.getTeam() == null && entityIn.getTeam() == null;
            return false;
        }
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getAmbientSound() {
        return DTESoundRegistry.GAOLER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return DTESoundRegistry.GAOLER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return DTESoundRegistry.GAOLER_DEATH.get();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(DTESoundRegistry.GAOLER_STEP.get(), 10.0F, 1.0F);
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
        CameraShakeManager.addCameraShake(new CameraShakeData(15, this.position(), 12));
        MagicManager.spawnParticles(entity.level(), new BlastwaveParticleOptions(SchoolRegistry.ELDRITCH.get().getTargetingColor(), 5),
                entity.getX(), 0.3, entity.getZ(), 1, 0, 0, 0, 0, true);

        return Utils.doMeleeAttack(this, entity, SpellRegistries.CONJURE_GAOLER.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean canDisableShield() {
        return true;
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    private int getHeartBeatDelay()
    {
        return 40 - Mth.floor(Mth.clamp(20, 0.0F, 1.0F) * 30.0F);
    }

    public void applyDarknessAround(ServerLevel level, Vec3 pos, @Nullable Entity source, int radius) {
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
        MobEffectUtil.addEffectToPlayersAround(level, source, pos, radius, mobeffectinstance, 200);

        List<LivingEntity> entitiesNearby = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));
        for (LivingEntity targets : entitiesNearby)
        {
            targets.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 1, false, false, false));
        }
    }

    // Geckolib & Animations
    RawAnimation animationToPlay = null;
    private final AnimationController<GaolerEntity> animationController = new AnimationController<>(this, "controller", 0, this::predicate);
    private final AnimationController<GaolerEntity> attackAnimationController = new AnimationController<>(this, "attack_controller", 0, this::attackPredicate);
    private final AnimationController<GaolerEntity> castingAnimationController = new AnimationController<>(this, "casting_controller", 0, this::castingPredicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(animationController);
        controllers.add(attackAnimationController);
        controllers.add(castingAnimationController);
    }

    private PlayState predicate(AnimationState<GaolerEntity> event)
    {
        if (!isPlayingRiseAnimation())
        {
            if (event.isMoving() && this.animationToPlay == null)
            {
                event.getController().setAnimation(RawAnimation.begin().then("walking", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
            else if (!event.isMoving() && this.animationToPlay == null)
            {
                event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        }
        else
        {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState attackPredicate(AnimationState<GaolerEntity> event)
    {
        var controller = event.getController();

        if (!isPlayingRiseAnimation())
        {
            if (this.animationToPlay != null)
            {
                // This should do the custom attack animations
                controller.forceAnimationReset();
                controller.setAnimation(animationToPlay);
                animationToPlay = null;
            }
        }
        else
        {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
        }

        return PlayState.CONTINUE;
    }

    private PlayState castingPredicate(AnimationState<GaolerEntity> event)
    {
        if (!isPlayingRiseAnimation())
        {
            if (isCasting() && this.animationToPlay == null)
            {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("charged_spit"));
                return PlayState.CONTINUE;
            }
            else
            {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
            }
        }

        return PlayState.STOP;
    }

    @Override
    public void playAnimation(String animationId) {
        try {
            animationToPlay = RawAnimation.begin().thenPlay(animationId);
        } catch (Exception ignored) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", this, animationId);
        }
    }

    @Override
    public boolean isAnimating() {
        return animationController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
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
                livingEntity.playSound(SoundEvents.WARDEN_DIG, 10.0F, this.getVoicePitch());
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
    protected void customServerAiStep() {
        // Darkness
        super.customServerAiStep();

        ServerLevel serverLevel = (ServerLevel) this.level();
        if (this.tickCount % 120 == 0)
        {
            applyDarknessAround(serverLevel, this.position(), this, 25);
        }
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
        // Client side stuff
        if (this.level().isClientSide())
        {
            if (this.tickCount % this.getHeartBeatDelay() == 0)
            {
                if (!this.isSilent()) {
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.WARDEN_HEARTBEAT, this.getSoundSource(), 5.0F, this.getVoicePitch(), false);
                }
            }
            // Screenshake when it walks
            if (this.getDeltaMovement().x < 0 || this.getDeltaMovement().z < 0)
            {
                CameraShakeManager.addCameraShake(new CameraShakeData(4, this.position(), 20));
            }
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
