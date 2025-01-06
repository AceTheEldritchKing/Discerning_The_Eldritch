package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ApothicAcolyteEntity extends NeutralWizard implements Enemy {
    public ApothicAcolyteEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 25;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 1, 5, 100, 250, 1));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 40, 75)
                .setSpells(
                        // Attack
                        List.of(SpellRegistry.SHOCKWAVE_SPELL.get(), SpellRegistry.SONIC_BOOM_SPELL.get(), SpellRegistry.BALL_LIGHTNING_SPELL.get()),
                        // Defense
                        List.of(SpellRegistry.COUNTERSPELL_SPELL.get(), SpellRegistry.HEAL_SPELL.get(), SpellRegistry.BLIGHT_SPELL.get()),
                        // Movement
                        List.of(SpellRegistry.BLOOD_STEP_SPELL.get()),
                        // Support
                        List.of(SpellRegistry.ABYSSAL_SHROUD_SPELL.get())
                        // Silence down here is a temp thing
                ).setSingleUseSpell(SpellRegistry.SCULK_TENTACLES_SPELL.get(), 80, 400, 1, 3)
                .setDrinksPotions());
        //this.goalSelector.addGoal(4, new PatrolNearLocationGoal(this, 30, .75f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this)
        {
            return true;
        }
        else if (entityIn instanceof IMagicSummon summon && summon.getSummoner() == this)
        {
            return true;
        }
        else if (entityIn instanceof ApothicCrusaderEntity || entityIn instanceof ApothicAcolyteEntity)
        {
            return true;
        }
        else
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.GECKOLIB_ELDRITCH_WARLOCK_HOOD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.GECKOLIB_ELDRITCH_WARLOCK_ROBES.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.GECKOLIB_ELDRITCH_WARLOCK_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.GECKOLIB_ELDRITCH_WARLOCK_GREAVES.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistries.RITUAL_DAGGER.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
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
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, .15);
    }
}
