package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.util.Log;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashMap;

// I have to create the goddamn AbstractSpellCastingMob class because of my little entities will not animate
// if their mother does not provide them with a special class (they will crash the game)
public abstract class UniqueAbstractSpellCastingMob extends PathfinderMob implements GeoEntity, IMagicEntity {
    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.BOOLEAN);
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(IronsSpellbooks.id("potion_slowdown"), -0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private SpellData castingSpell;
    protected AbstractSpell lastCastSpellType = SpellRegistry.none();
    protected AbstractSpell instantCastSpellType = SpellRegistry.none();
    protected boolean cancelCastAnimation = false;
    protected boolean animatingLegs = false;
    private final MagicData playerMagicData = new MagicData(true);
    private boolean recreateSpell;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final HashMap<String, AbstractSpell> spells = Maps.newHashMap();
    private int drinkTime;
    public boolean hasUsedSingleAttack;

    public UniqueAbstractSpellCastingMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.playerMagicData.setSyncedData(new SyncedSpellData(this));
        this.noCulling = true;
        this.lookControl = this.createLookControl();
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

    public boolean getHasUsedSingleAttack() {
        return hasUsedSingleAttack;
    }

    @Override
    public void setHasUsedSingleAttack(boolean hasUsedSingleAttack) {
        this.hasUsedSingleAttack = hasUsedSingleAttack;
    }

    public MagicData getMagicData() {
        return this.playerMagicData;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (this.level().isClientSide) {
            if (pKey.id() == DATA_CANCEL_CAST.id()) {
                IronsSpellbooks.LOGGER.debug("ASCM.onSyncedDataUpdated.1 this.isCasting:{}, playerMagicData.isCasting:{} isClient:{}", new Object[]{this.isCasting(), this.playerMagicData == null ? "null" : this.playerMagicData.isCasting(), this.level().isClientSide()});
                this.cancelCast();
            }

        }
    }

    public boolean isDrinkingPotion() {
        return entityData.get(DATA_DRINKING_POTION);
    }

    protected void setDrinkingPotion(boolean drinkingPotion) {
        this.entityData.set(DATA_DRINKING_POTION, drinkingPotion);
    }

    public void startDrinkingPotion() {
        if (!level().isClientSide) {
            setDrinkingPotion(true);
            drinkTime = 35;
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
    }

    private void finishDrinkingPotion() {
        setDrinkingPotion(false);
        this.heal(Math.min(Math.max(10, getMaxHealth() / 10), getMaxHealth() / 4));
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
    }

    public void cancelCast() {
        if (this.isCasting()) {
            if (this.level().isClientSide) {
                this.cancelCastAnimation = true;
            } else {
                this.entityData.set(DATA_CANCEL_CAST, !(Boolean)this.entityData.get(DATA_CANCEL_CAST));
            }

            this.castComplete();
        }

    }

    public void castComplete() {
        if (!this.level().isClientSide) {
            if (this.castingSpell != null) {
                this.castingSpell.getSpell().onServerCastComplete(this.level(), this.castingSpell.getLevel(), this, this.playerMagicData, false);
            }
        } else {
            this.playerMagicData.resetCastingState();
        }

        this.castingSpell = null;
    }

    public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
        if (this.level().isClientSide) {
            boolean isCasting = this.playerMagicData.isCasting();
            this.playerMagicData.setSyncedData(syncedSpellData);
            this.castingSpell = this.playerMagicData.getCastingSpell();
            IronsSpellbooks.LOGGER.debug("ASCM.setSyncedSpellData playerMagicData:{}, priorIsCastingState:{}, spell:{}", new Object[]{this.playerMagicData, isCasting, this.castingSpell});
            if (this.castingSpell != null) {
                if (!this.playerMagicData.isCasting() && isCasting) {
                    this.castComplete();
                } else if (this.playerMagicData.isCasting() && !isCasting) {
                    AbstractSpell spell = this.playerMagicData.getCastingSpell().getSpell();
                    this.initiateCastSpell(spell, this.playerMagicData.getCastingSpellLevel());
                    if (this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                        this.instantCastSpellType = this.castingSpell.getSpell();
                        this.castingSpell.getSpell().onClientPreCast(this.level(), this.castingSpell.getLevel(), this, InteractionHand.MAIN_HAND, this.playerMagicData);
                        this.castComplete();
                    }
                }

            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (recreateSpell) {
            recreateSpell = false;
            var syncedSpellData = playerMagicData.getSyncedData();
            var spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
            //setSyncedSpellData(syncedSpellData);
        }

        if (isDrinkingPotion()) {
            if (drinkTime-- <= 0) {
                finishDrinkingPotion();
            } else if (drinkTime % 4 == 0) {
                if (!this.isSilent()) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_DRINK, this.getSoundSource(), 1.0F, Utils.random.nextFloat() * 0.1F + 0.9F);
                }
            }
        }

        if (castingSpell == null) {
            return;
        }

        playerMagicData.handleCastDuration();

        if (playerMagicData.isCasting()) {
            castingSpell.getSpell().onServerCastTick(level(), castingSpell.getLevel(), this, playerMagicData);
        }

        if (Log.SPELL_DEBUG) {
            IronsSpellbooks.LOGGER.debug("ASCM.customServerAiStep.1");
        }

        this.forceLookAtTarget(getTarget());

        if (playerMagicData.getCastDurationRemaining() <= 0) {
            if (Log.SPELL_DEBUG) {
                IronsSpellbooks.LOGGER.debug("ASCM.customServerAiStep.2");
            }

            if (castingSpell.getSpell().getCastType() == CastType.LONG || castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                if (Log.SPELL_DEBUG) {
                    IronsSpellbooks.LOGGER.debug("ASCM.customServerAiStep.3");
                }
                castingSpell.getSpell().onCast(level(), castingSpell.getLevel(), this, CastSource.MOB, playerMagicData);
            }
            castComplete();
        } else if (castingSpell.getSpell().getCastType() == CastType.CONTINUOUS) {
            if ((playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                castingSpell.getSpell().onCast(level(), castingSpell.getLevel(), this, CastSource.MOB, playerMagicData);
            }
        }
    }

    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        IronsSpellbooks.LOGGER.debug("ASCM.initiateCastSpell: spellType:{} spellLevel:{}, isClient:{}", new Object[]{spell.getSpellId(), spellLevel, this.level().isClientSide});
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
        } else {
            if (this.level().isClientSide) {
                this.cancelCastAnimation = false;
            }

            this.castingSpell = new SpellData(spell, spellLevel);
            if (this.getTarget() != null) {
                this.forceLookAtTarget(this.getTarget());
            }

            if (!this.level().isClientSide && !this.castingSpell.getSpell().checkPreCastConditions(this.level(), spellLevel, this, this.playerMagicData)) {
                IronsSpellbooks.LOGGER.debug("ASCM.precastfailed: spellType:{} spellLevel:{}, isClient:{}", new Object[]{spell.getSpellId(), spellLevel, this.level().isClientSide});
                this.castingSpell = null;
            } else {
                if (spell != SpellRegistry.TELEPORT_SPELL.get() && spell != SpellRegistry.FROST_STEP_SPELL.get()) {
                    if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
                        this.setTeleportLocationBehindTarget(3);
                    } else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
                        this.setBurningDashDirectionData();
                    }
                } else {
                    this.setTeleportLocationBehindTarget(10);
                }

                this.playerMagicData.initiateCast(this.castingSpell.getSpell(), this.castingSpell.getLevel(), this.castingSpell.getSpell().getEffectiveCastTime(this.castingSpell.getLevel(), this), CastSource.MOB, SpellSelectionManager.MAINHAND);
                if (!this.level().isClientSide) {
                    this.castingSpell.getSpell().onServerPreCast(this.level(), this.castingSpell.getLevel(), this, this.playerMagicData);
                }

            }
        }
    }

    public void notifyDangerousProjectile(Projectile projectile) {
    }

    public boolean isCasting() {
        return this.playerMagicData.isCasting();
    }

    public boolean setTeleportLocationBehindTarget(int distance) {
        var target = getTarget();
        boolean valid = false;
        if (target != null) {
            var rotation = target.getLookAngle().normalize().scale(-distance);
            var pos = target.position();
            var teleportPos = rotation.add(pos);

            for (int i = 0; i < 24; i++) {
                Vec3 randomness = Utils.getRandomVec3(.15f * i).multiply(1, 0, 1);
                teleportPos = Utils.moveToRelativeGroundLevel(level(), target.position().subtract(new Vec3(0, 0, distance / (float) (i / 7 + 1)).yRot(-(target.getYRot() + i * 45) * Mth.DEG_TO_RAD)).add(randomness), 5);
                teleportPos = new Vec3(teleportPos.x, teleportPos.y + .1f, teleportPos.z);
                var reposBB = this.getBoundingBox().move(teleportPos.subtract(this.position()));
                //IronsSpellbooks.LOGGER.debug("setTeleportLocationBehindTarget attempt to teleport to {}:", reposBB.getCenter());
                if (!level().collidesWithSuffocatingBlock(this, reposBB.inflate(-.05f))) {
                    //IronsSpellbooks.LOGGER.debug("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\nsetTeleportLocationBehindTarget: {} {} {} empty. teleporting\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n\n", reposBB.minX, reposBB.minY, reposBB.minZ);
                    valid = true;
                    break;
                }
                //IronsSpellbooks.LOGGER.debug("fail");

            }
            if (valid) {
                if (Log.SPELL_DEBUG) {
                    //IronsSpellbooks.LOGGER.debug("ASCM.setTeleportLocationBehindTarget: valid, pos:{}, isClient:{}", teleportPos, level.isClientSide());
                }
                playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(teleportPos));
            } else {
                if (Log.SPELL_DEBUG) {
                    //IronsSpellbooks.LOGGER.debug("ASCM.setTeleportLocationBehindTarget: invalid, pos:{}, isClient:{}", teleportPos, level.isClientSide());
                }
                playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.position()));

            }
        } else {
            if (Log.SPELL_DEBUG) {
                //IronsSpellbooks.LOGGER.debug("ASCM.setTeleportLocationBehindTarget: no target, isClient:{}", level.isClientSide());
            }
            playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.position()));
        }
        return valid;
    }

    public void setBurningDashDirectionData() {
        this.playerMagicData.setAdditionalCastData(new BurningDashSpell.BurningDashDirectionOverrideCastData());
    }

    private void forceLookAtTarget(LivingEntity target) {
        if (target != null) {
            double d0 = target.getX() - this.getX();
            double d2 = target.getZ() - this.getZ();
            double d1 = target.getEyeY() - this.getEyeY();

            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
            this.setXRot(f1 % 360);
            this.setYRot(f % 360);
        }
    }

    // Geckolib & Animations
    // I'm tired, forgive me for copying code </3
    private final AnimationController animationController = new AnimationController<>(this, "controller", 0, this::predicate);
    private final AnimationController instantCastAnimationController = new AnimationController<>(this, "instant_cast_controller", 0, this::instantCastPredicate);
    private final AnimationController longCastAnimationController = new AnimationController<>(this, "long_cast_controller", 0, this::longCastPredicate);
    private final AnimationController contCastAnimationController = new AnimationController<>(this, "continuous_cast_controller", 0, this::continuousCastPredicate);
    private final AnimationController castingAnimationController = new AnimationController<>(this, "casting_controller", 0, this::castingPredicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(animationController);
        controllers.add(instantCastAnimationController);
        controllers.add(longCastAnimationController);
        controllers.add(contCastAnimationController);
    }

    private PlayState predicate(AnimationState event)
    {
        if (event.isMoving())
        {
            //System.out.println("Set is moving");
            event.getController().setAnimation(RawAnimation.begin().then("walking", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        else if (!event.isMoving())
        {
            //System.out.println("Set is idle");
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState instantCastPredicate(AnimationState event)
    {
        //System.out.println("Instant predicate");
        if (cancelCastAnimation)
        {
            return PlayState.STOP;
        }

        var controller = event.getController();
        if (instantCastSpellType != SpellRegistry.none() && controller.getAnimationState() == AnimationController.State.STOPPED)
        {
            setStartAnimationFromSpell(controller, instantCastSpellType);
            instantCastSpellType = SpellRegistry.none();
        }
        return PlayState.CONTINUE;
    }

    private PlayState longCastPredicate(AnimationState event)
    {
        //System.out.println("Long predicate");
        var controller = event.getController();

        if (cancelCastAnimation || (controller.getAnimationState() == AnimationController.State.STOPPED && !(isCasting() && castingSpell != null && castingSpell.getSpell().getCastType() == CastType.LONG)))
        {
            return PlayState.STOP;
        }

        if (isCasting() && this.castingSpell != null)
        {
            //System.out.println("Is casting?");
            if (controller.getAnimationState() == AnimationController.State.STOPPED)
            {
                //System.out.println("Set long cast animation");
                setStartAnimationFromSpell(controller, castingSpell.getSpell());
            }
        }

        return PlayState.CONTINUE;
    }

    private PlayState continuousCastPredicate(AnimationState event)
    {
        //System.out.println("Continuous predicate");
        var controller = event.getController();

        if (cancelCastAnimation || (controller.getAnimationState() == AnimationController.State.STOPPED && !(isCasting() && castingSpell != null && castingSpell.getSpell().getCastType() == CastType.LONG)))
        {
            return PlayState.STOP;
        }

        if (isCasting() && this.castingSpell != null)
        {
            //System.out.println("Is casting?");
            if (controller.getAnimationState() == AnimationController.State.STOPPED)
            {
                //System.out.println("Set continuous cast animation");
                setStartAnimationFromSpell(controller, castingSpell.getSpell());
            }
        }

        return PlayState.CONTINUE;
    }

    // For testing purposes
    private PlayState castingPredicate(AnimationState event)
    {
        var controller = event.getController();

        if (isCasting())
        {
            controller.forceAnimationReset();
            controller.setAnimation(RawAnimation.begin().thenPlay("long_cast"));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    protected void setStartAnimationFromSpell(AnimationController controller, AbstractSpell spell) {
        spell.getCastStartAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            if(DTEUtils.isLongAnimCast(spell)) {
                //System.out.println("Set Start long cast");
                controller.setAnimation(RawAnimation.begin().then("long_cast", Animation.LoopType.PLAY_ONCE));
            }
            else if (DTEUtils.isContAnimCast(spell)) {
                //System.out.println("Set Start cont. cast");
                controller.setAnimation(RawAnimation.begin().then("continous_cast", Animation.LoopType.PLAY_ONCE));
            }
            else {
                //System.out.println("Set Start instant cast");
                controller.setAnimation(RawAnimation.begin().then("instant_cast", Animation.LoopType.PLAY_ONCE));
            }
            lastCastSpellType = spell;
            cancelCastAnimation = false;
            animatingLegs = false;
        }, () -> {
            cancelCastAnimation = true;
        });
    }

    public boolean isAnimating() {
        return this.isCasting() || this.longCastAnimationController.getAnimationState() == AnimationController.State.RUNNING || this.contCastAnimationController.getAnimationState() == AnimationController.State.RUNNING || this.instantCastAnimationController.getAnimationState() == AnimationController.State.RUNNING;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // NBT
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        SyncedSpellData syncedSpellData = new SyncedSpellData(this);
        syncedSpellData.loadNBTData(pCompound, this.level().registryAccess());
        if (syncedSpellData.isCasting()) {
            this.recreateSpell = true;
        }

        this.playerMagicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = pCompound.getBoolean("usedSpecial");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.playerMagicData.getSyncedData().saveNBTData(pCompound, this.level().registryAccess());
        pCompound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CANCEL_CAST, false);
        builder.define(DATA_DRINKING_POTION, false);
    }
}
