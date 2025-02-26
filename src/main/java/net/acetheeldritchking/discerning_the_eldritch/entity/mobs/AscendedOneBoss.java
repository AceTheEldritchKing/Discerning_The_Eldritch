package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.boss_music.AscendedOneMusicManager;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AscendedOneBoss extends AbstractSpellCastingMob implements Enemy, IClientEventEntity {
    /*public AscendedOneBoss(Level level)
    {
        this(DTEEntityRegistry.ASCENDED_ONE.get(), level);
        setPersistenceRequired();
    }*/

    public AscendedOneBoss(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setPersistenceRequired();
        xpReward = 60;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.translatable("discerning_the_eldritch:ascended_one_boss"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final static EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AscendedOneBoss.class, EntityDataSerializers.INT);
    public static final byte STOP_MUSIC = 0;
    public static final byte START_MUSIC = 1;

    @Override
    public void handleClientEvent(byte eventId)
    {
        switch (eventId)
        {
            case STOP_MUSIC -> AscendedOneMusicManager.stop(this);
            case START_MUSIC -> AscendedOneMusicManager.createOrResumeInstance(this);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new EntityEventPacket<AscendedOneBoss>(this, START_MUSIC));
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new EntityEventPacket<AscendedOneBoss>(this, STOP_MUSIC));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    // Phase stuff //
    public enum Phase
    {
        FirstPhase(0),
        SecondPhase(1);

        final int value;

        Phase(int value)
        {
            this.value = value;
        }
    }

    private void setPhase(int phase)
    {
        this.entityData.set(PHASE, phase);
    }

    private void setPhase(Phase phase)
    {
        this.setPhase(phase.value);
    }

    public int getPhase()
    {
        return this.entityData.get(PHASE);
    }

    public boolean isPhase(Phase phase)
    {
        return phase.value == getPhase();
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
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        firstPhaseGoals();
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    private void firstPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 80, 150, 1));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 30, 55)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHOCKWAVE_SPELL.get(),
                                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                                SpellRegistry.BLOOD_NEEDLES_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.BLOOD_STEP_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.RAISE_DEAD_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.BOOGIE_WOOGIE.get(), 70, 100, 3, 5)
                .setSpellQuality(1.0f, 1.0f)
                .setDrinksPotions());
            this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    private void secondPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 30, 50, 5));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ABYSSAL_SHROUD_SPELL.get(), 1, 3, 80, 100, 0));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 20, 35)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistries.ESOTERIC_EDGE.get(),
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHOCKWAVE_SPELL.get(),
                                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                                SpellRegistry.ACUPUNCTURE_SPELL.get(),
                                SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                                SpellRegistry.SCULK_TENTACLES_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.BLIGHT_SPELL.get(),
                                SpellRegistry.SLOW_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.BLOOD_STEP_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.BLOOD_STEP_SPELL.get(),
                                SpellRegistry.PLANAR_SIGHT_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.RAISE_DEAD_SPELL.get(),
                                SpellRegistry.SUMMON_VEX_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get()
                        )
                        // Silence down here is a temp thing
                ).setSingleUseSpell(SpellRegistries.SILENCE.get(), 100, 250, 5, 5)
                .setSpellQuality(1.5f, 1.5f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    public void tick() {
        super.tick();

        float health = this.getHealth();
        float MAX_HEALTH = this.getMaxHealth();

        float halfHealth = MAX_HEALTH/2;

        if (isPhase(Phase.FirstPhase))
        {
            if (this.getHealth() <= halfHealth)
            {
                setPhase(Phase.SecondPhase);
                setHealth(halfHealth);

                secondPhaseGoals();

                this.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(1.5F);
            }
        }

        if (isPhase(Phase.SecondPhase))
        {
            int radius = 15;

            List<LivingEntity> entitiesNearby = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));

            for (LivingEntity targets : entitiesNearby)
            {
                if (targets instanceof ServerPlayer player)
                {
                    player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("discerning_the_eldritch:ascended_one_taunt")
                            .withStyle(s -> s.withColor(TextColor.fromRgb(0xC71B8B)))));
                }
            }

            this.bossEvent.setProgress(health / (MAX_HEALTH - halfHealth));
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof IMagicSummon summon && summon.getSummoner() == this)
        {
            return true;
        }
        // Should eventually replace this with mob tags instead
        else if (entityIn instanceof ApothicCrusaderEntity || entityIn instanceof ApothicSummonerEntity || entityIn instanceof ApothicAcolyteEntity)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.ASCENDED_ONE_HOOD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.ASCENDED_ONE_ROBES.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.ASCENDED_ONE_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.ASCENDED_ONE_GREAVES.get()));
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
                .add(Attributes.ATTACK_DAMAGE, 15.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.5)
                .add(Attributes.MAX_HEALTH, 550.0)
                .add(Attributes.ARMOR, 60)
                .add(Attributes.ARMOR_TOUGHNESS, 20)
                .add(Attributes.FOLLOW_RANGE, 80.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(AttributeRegistry.SPELL_POWER, 1.35)
                .add(AttributeRegistry.SPELL_RESIST, 1.7)
                ;
    }

    // NBT
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        pCompound.putInt("phase", getPhase());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.hasCustomName())
        {
            this.bossEvent.setName(this.getDisplayName());
        }
        setPhase(pCompound.getInt("phase"));
        if (isPhase(Phase.SecondPhase))
        {
            secondPhaseGoals();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PHASE, 0);
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }
}
