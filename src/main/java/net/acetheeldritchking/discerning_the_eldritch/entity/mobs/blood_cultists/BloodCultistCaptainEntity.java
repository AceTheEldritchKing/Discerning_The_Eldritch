package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.blood_cultists;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.minibosses.blood_matriarch.BloodMatriarchEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;

public class BloodCultistCaptainEntity extends BloodMageEntity implements Enemy, IAnimatedAttacker {
    public BloodCultistCaptainEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 25;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.BLOOD_NEEDLES_SPELL.get(), 1, 5, 100, 250, 2));
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 32, .75f));

        this.goalSelector.addGoal(3, new GenericAnimatedWarlockAttackGoal<>(this, 1.5f, 25, 40)
                .setMoveset(List.of(
                        new AttackAnimationData(9, "simple_sword_upward_swipe", 5),
                        new AttackAnimationData(8, "simple_sword_lunge_stab", 6),
                        new AttackAnimationData(10, "simple_sword_stab_alternate", 8),
                        new AttackAnimationData(10, "simple_sword_horizontal_cross_swipe", 8),
                        new AttackAnimationData(17, "overhead_sword_slam", 13),
                        new AttackAnimationData(24, "spin_slash_normal", 12, 15, 19, 22)
                ))
                .setComboChance(0.8F)
                .setMeleeAttackInverval(10, 15)
                .setMeleeMovespeedModifier(1.5F)
                .setMeleeBias(0.3f, 0.8f)
                .setSpells(
                        // Attack
                        List.of(SpellRegistry.BLOOD_NEEDLES_SPELL.get(), SpellRegistry.BLOOD_NEEDLES_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.BLOOD_SLASH_SPELL.get()),
                        // Defense
                        List.of(SpellRegistry.COUNTERSPELL_SPELL.get(), SpellRegistry.HEAL_SPELL.get(), SpellRegistry.CHARGE_SPELL.get(), SpellRegistry.RAY_OF_SIPHONING_SPELL.get()),
                        // Movement
                        List.of(SpellRegistry.BLOOD_STEP_SPELL.get()),
                        // Support
                        List.of(SpellRegistry.HEARTSTOP_SPELL.get())
                ).setSingleUseSpell(SpellRegistry.ACUPUNCTURE_SPELL.get(), 50, 100, 2, 5)
                .setSpellQuality(1.0f, 1.0f)
                .setDrinksPotions()
        );
        //this.goalSelector.addGoal(4, new PatrolNearLocationGoal(this, 30, .75f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        // They HATE these guys  - But not YOU
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicCrusaderEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicAcolyteEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicSummonerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AscendedOneBoss.class, true));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.CULTIST_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistry.CULTIST_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.CULTIST_BOOTS.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.MISERY.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemRegistries.RAZOR_SHEATH.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.01F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.45F);
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof IMagicSummon summon && summon.getSummoner() == this)
        {
            return true;
        }
        else if (entityIn.getType().is(DTETags.BLOOD_ALLIES))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.MAX_HEALTH, 85.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, .15)
                .add(AttributeRegistry.MAX_MANA, 500)
                .add(Attributes.SCALE, 1.2);
    }

    @Override
    public boolean shouldSheathSword() {
        return true;
    }

    RawAnimation animationToPlay = null;
    private final AnimationController<BloodCultistCaptainEntity> meleeController = new AnimationController<>(this, "keeper_animations", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public void playAnimation(String animationId) {
        animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState predicate(AnimationState<BloodCultistCaptainEntity> animationState)
    {
        var controller = animationState.getController();

        if (this.animationToPlay != null)
        {
            controller.forceAnimationReset();
            controller.setAnimation(animationToPlay);
            animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isAnimating() {
        return meleeController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new NotIdioticNavigation(this, level);
    }

    @Override
    public boolean isHostileTowards(LivingEntity pTarget) {
        return super.isHostileTowards(pTarget) || pTarget.getAttributeValue(AttributeRegistry.BLOOD_SPELL_POWER) < 1.15;
    }
}
