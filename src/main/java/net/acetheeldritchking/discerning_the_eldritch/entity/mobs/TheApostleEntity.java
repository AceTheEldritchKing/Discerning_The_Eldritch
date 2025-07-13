package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.acetheeldritchking.aces_spell_utils.entity.mobs.UniqueAbstractSpellCastingMob;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.WizardSpellComboGoal;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TheApostleEntity extends UniqueAbstractSpellCastingMob implements IMagicSummon, GeoAnimatable, IMagicEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public TheApostleEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 0;
        this.lookControl = createLookControl();
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public TheApostleEntity(Level level, LivingEntity owner) {
        this(DTEEntityRegistry.APOSTLE_ENTITY.get(), level);
        setSummoner(owner);
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

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(true);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public @org.jetbrains.annotations.Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnGroupData) {
        this.setNoGravity(true);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new FloatGoal(this));

        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 80, 150, 1));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 30, 55)
                .setSpells(
                        // Attack
                        List.of(
                                //SpellRegistries.ESOTERIC_EDGE.get(),
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.ACUPUNCTURE_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.FIRE_ARROW_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.BLIGHT_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.THUNDERSTORM_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.TELEPORT_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.HASTE_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.SILENCE.get(), 100, 250, 5, 5)
                .setSpellQuality(1.0f, 1.0f)
                .setIsFlying()
                .setSpellQuality(0.8f, 0.8f)
                .setAllowFleeing(false)
        );

        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 10, 1, true, 50));
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
                .add(Attributes.ATTACK_DAMAGE, 15.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.FOLLOW_RANGE, 45.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 0.25)
                .add(AttributeRegistry.SPELL_POWER, 1.1)
                .add(AttributeRegistry.SPELL_RESIST, 1.1)
                ;
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

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    // Attacks and Death
    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromLevel() {
        this.onRemovedHelper(this, DTEPotionEffectRegistry.FORSAKEN_TIMER);
        super.onRemovedFromLevel();
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide)
        {
            MagicManager.spawnParticles(this.level(), ParticleTypes.ENCHANT,
                    getX(), getY(), getZ(),
                    25, 0.4, 0.8, 0.4, 0.03, false);
            discard();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return Utils.doMeleeAttack(this, entity, SpellRegistries.CONJURE_FORSAKE_AID.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // No fall damage please <3
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.shouldIgnoreDamage(pSource) ? false : super.hurt(pSource, pAmount);
    }

    // Geckolib & Animations
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
}
