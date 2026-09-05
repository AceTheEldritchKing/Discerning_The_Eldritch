package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.BossbarManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.goals.MomentHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.ExtendedServerBossEvent;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.acetheeldritchking.aces_spell_utils.entity.mobs.GenericBossEntity;
import net.acetheeldritchking.aces_spell_utils.entity.mobs.goals.WizardSpellComboGoal;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.aces_spell_utils.utils.boss_music.BossMusicManager;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicAcolyteEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicCrusaderEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicSummonerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkAttackKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkBlockKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.apostle_of_sculk.keyframes.ApostleOfSculkHeavyAttackKeyframe;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one.AscendedOneBoss;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk.ApostleOfSculkAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk.SculkApostleAoECounterspellAbilityGoal;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk.SculkApostleAoESculkSlam;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.apostle_of_sculk.SculkApostleSummonAbilityGoal;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEServerConfig;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;

public class ApostleOfSculkBoss extends GenericBossEntity implements IAnimatedAttacker, IEntityWithComplexSpawn {
    // This method is only needed if you plan on summoning the boss with a spell
    // And Torment Mode, that too
    public ApostleOfSculkBoss(Level level, boolean isTormentMode)
    {
        this(DTEEntityRegistry.APOSTLE_OF_SCULK.get(), level);
        setPersistenceRequired();
        setTormentMode(isTormentMode);
    }

    public ApostleOfSculkBoss(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setPersistenceRequired();
        xpReward = 60;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
        createBossEvent();
    }

    // Boss Bar
    private static final BossbarManager.BossbarSprite BOSSBAR_SPRITE = new BossbarManager.BossbarSprite(DiscerningTheEldritch.id("boss_bars/apostle_of_sculk_boss_bar"), 192, 18, 3, -1);

    // These are used for doing boss bars, setting up the phase serializer for NBT, and stopping and starting music
    private ExtendedServerBossEvent bossEvent;
    private final static EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(ApostleOfSculkBoss.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Integer> RAGE_METER = SynchedEntityData.defineId(ApostleOfSculkBoss.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(ApostleOfSculkBoss.class, EntityDataSerializers.BOOLEAN);
    private final static EntityDataAccessor<Boolean> TORMENT_MODE = SynchedEntityData.defineId(ApostleOfSculkBoss.class, EntityDataSerializers.BOOLEAN);
    public static final byte CLIENT_STOP_TRACKING = 0;
    public static final byte CLIENT_START_TRACKING = 1;
    public static final byte PROC_PARRY = 2;
    public static final byte PROC_RAGE_QUIT = 3;
    public static final byte START_MUSIC = 4;
    public static final byte STOP_MUSIC = 5;

    // Boss music
    public static SoundEvent bossMusic = DTESoundRegistry.APOSTLE_OF_SCULK_THEME.get();
    public static SoundEvent bossTransitionMusic = DTESoundRegistry.APOSTLE_OF_SCULK_THEME.get();
    public static SoundEvent bossFinalMusic = DTESoundRegistry.APOSTLE_OF_SCULK_THEME.get();

    // Animation ticks
    public int transitionAnimationTime = 180;
    public int deathAnimationTime = 360;
    int spawnTimer;
    private static final int spawnAnimTime = (int) (7.59 * 20);
    private static final int spawnDelay = 20;
    int parryTime;
    int parryCooldown;
    // The amount of time it takes to build up rage again
    int rageTime;
    int rageCooldown;
    public float animDampener;

    // Loot
    SimpleContainer deathLoot = null;

    // Block Destroying
    private int destroyBlockDelay;
    private int stuckDetectorDelay;
    private int stuckDetector;
    private Vec3 lastStuck = Vec3.ZERO;

    // Music
    @Override
    public boolean hasCustomMusic() {
        return true;
    }

    @Override
    public boolean changeMusicOnPhaseChange() {
        return false;
    }

    @Override
    public boolean hasTransitionPhase() {
        return false;
    }

    @Override
    public int usePhaseAsTransition() {
        return 2;
    }

    @Override
    public int usePhaseForMusicChange() {
        return 3;
    }

    @Override
    public SoundEvent getBossMusic() {
        return bossMusic;
    }

    @Override
    public SoundEvent getTransitionMusic() {
        return bossTransitionMusic;
    }

    @Override
    public SoundEvent getOtherPhaseMusic() {
        return bossFinalMusic;
    }

    // Helps handle the starting and stopping of boss music
    @Override
    public void handleClientEvent(byte eventId)
    {
        switch (eventId)
        {
            case CLIENT_STOP_TRACKING -> {
                BossbarManager.stopTracking(this.uuid);
                BossMusicManager.stop(this);
            }
            case CLIENT_START_TRACKING ->
            {
                BossbarManager.startTracking(this.uuid, BOSSBAR_SPRITE);
                if (!isSpawning())
                {
                    BossMusicManager.createOrResumeInstance(this);
                }
            }
            case PROC_PARRY -> procParry();
            case PROC_RAGE_QUIT -> procEnraged();
            case START_MUSIC -> BossMusicManager.createOrResumeInstance(this);
            case STOP_MUSIC -> BossMusicManager.stop(this);
        }
    }

    // These two methods add and remove the boss bar and music based on how far the player is/if it is seen by the boss
    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new EntityEventPacket<ApostleOfSculkBoss>(this, CLIENT_START_TRACKING));
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new EntityEventPacket<ApostleOfSculkBoss>(this, CLIENT_STOP_TRACKING));
    }

    // For updating the boss health
    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    // These are for movement and looking controls for smoother movement (from Iron himself)
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
    protected PathNavigation createNavigation(Level level) {
        return new NotIdioticNavigation(this, level);
    }

    // Register the basic goals for the boss
    @Override
    protected void registerGoals() {
        firstPhaseGoals();
        this.targetSelector.addGoal(1, new MomentHurtByTargetGoal(this));
        // She HATE these guys
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, KeeperEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PriestEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, FireBossEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, DeadKingBoss.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicSummonerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicAcolyteEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ApothicCrusaderEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AscendedOneBoss.class, true));
    }

    float meleeSpeedModifier = getEnraged() ? 2.5F : 1.5F;
    ApostleOfSculkAnimatedWarlockAttackGoal attackGoal = new ApostleOfSculkAnimatedWarlockAttackGoal(this, meleeSpeedModifier, 25, 40);


    // First phase spells
    private void firstPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic + Melee
        this.goalSelector.addGoal(1, new SculkApostleSummonAbilityGoal(this));

        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 1, 3, 80, 150, 3));
        this.goalSelector.addGoal(4, new SpellBarrageGoal(this, SpellRegistries.RIFT_WALKER.get(), 1, 3, 80, 150, 3));

        this.attackGoal = (ApostleOfSculkAnimatedWarlockAttackGoal) new ApostleOfSculkAnimatedWarlockAttackGoal(this, meleeSpeedModifier, 25, 40)
                .setMoveset(List.of(
                        new AttackAnimationData(50, "apostle_spear_attack", 14, 25),
                        new AttackAnimationData(20, "spin_slash_melee", 2, 5, 7, 10, 12, 15, 17, 20),
                        AttackAnimationData.builder("staff_right_spin_slice")
                                .length(55)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new AttackKeyframe(10, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(13, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new ApostleOfSculkBlockKeyframe(15),
                                        new AttackKeyframe(18, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(23, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(true, true)),
                                        new AttackKeyframe(40, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(42, new Vec3(0, 0.1, 1.85), new Vec3(0, .3, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(44, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(45, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(47, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(49, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(50, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(52, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true))
                                ).build(),
                        AttackAnimationData.builder("staff_upswing_slam_1")
                                .length(59)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(20, new Vec3(0, 0.25, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(44, new Vec3(0, 1, 0), new Vec3(0, 1.15, .1), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(48, new Vec3(0, -1, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(54, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build(),
                        AttackAnimationData.builder("apostle_spin_attack")
                                .length(87)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(22, new Vec3(0, 0.75, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(41, new Vec3(0, -0.75, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(45, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build()
                ))
                .setComboChance(0.8F)
                .setMeleeAttackInverval(50, 80)
                .setMeleeMovespeedModifier(meleeSpeedModifier)
                .setMeleeBias(0.2f, 0.3f)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHADOW_SLASH.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistries.MEND_FLESH.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistries.RIFT_WALKER.get(),
                                SpellRegistry.SHADOW_SLASH.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.ESOTERIC_EDGE.get(), 70, 100, 3, 5)
                .setSpellQuality(1.0f, 1.0f);

        this.goalSelector.addGoal(3, attackGoal);

        this.goalSelector.addGoal(3, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.SHADOW_SLASH.get()
                ), 1.3f, 1.3f, 80, 150));

        this.goalSelector.addGoal(4, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.COUNTERSPELL_SPELL.get(),
                        SpellRegistries.ESOTERIC_EDGE.get()
                ), 1.3f, 1.3f, 50, 80));

        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Second
    private void secondPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic + Melee
        this.goalSelector.addGoal(1, new SculkApostleAoECounterspellAbilityGoal(this));
        this.goalSelector.addGoal(1, new SculkApostleSummonAbilityGoal(this));

        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 1, 3, 80, 150, 3));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistries.RIFT_WALKER.get(), 1, 3, 80, 150, 3));

        this.attackGoal = (ApostleOfSculkAnimatedWarlockAttackGoal) new ApostleOfSculkAnimatedWarlockAttackGoal(this, meleeSpeedModifier, 25, 40)
                .setMoveset(List.of(
                        new AttackAnimationData(50, "apostle_spear_attack", 14, 25),
                        new AttackAnimationData(20, "spin_slash_melee", 2, 5, 7, 10, 12, 15, 17, 20),
                        AttackAnimationData.builder("staff_right_spin_slice")
                                .length(55)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new AttackKeyframe(10, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(13, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new ApostleOfSculkBlockKeyframe(15),
                                        new AttackKeyframe(18, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(23, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(true, true)),
                                        new AttackKeyframe(40, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(42, new Vec3(0, 0.1, 1.85), new Vec3(0, .3, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(44, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(45, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(47, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(49, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(50, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(52, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true))
                                ).build(),
                        AttackAnimationData.builder("staff_upswing_slam_1")
                                .length(59)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(20, new Vec3(0, 0.25, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(44, new Vec3(0, 1, 0), new Vec3(0, 1.15, .1), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(48, new Vec3(0, -1, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(54, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build(),
                        AttackAnimationData.builder("apostle_spin_attack")
                                .length(87)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(22, new Vec3(0, 0.75, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(41, new Vec3(0, -0.75, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(45, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build()
                ))
                .setComboChance(0.8F)
                .setMeleeAttackInverval(25, 50)
                .setMeleeMovespeedModifier(meleeSpeedModifier)
                .setMeleeBias(0.25f, 0.50f)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHADOW_SLASH.get(),
                                SpellRegistries.ESOTERIC_EDGE.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistries.MEND_FLESH.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistries.RIFT_WALKER.get(),
                                SpellRegistry.SHADOW_SLASH.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.ESOTERIC_EDGE.get(), 70, 100, 3, 5)
                .setSpellQuality(1.5f, 1.5f);

        this.goalSelector.addGoal(3, attackGoal);
        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.SHADOW_SLASH.get()
                ), 1.8f, 1.8f, 80, 150));

        this.goalSelector.addGoal(3, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.COUNTERSPELL_SPELL.get(),
                        SpellRegistries.ESOTERIC_EDGE.get()
                ), 1.8f, 1.8f, 50, 80));

        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Third
    private void thirdPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic + Melee
        this.goalSelector.addGoal(1, new SculkApostleAoECounterspellAbilityGoal(this));
        this.goalSelector.addGoal(1, new SculkApostleAoESculkSlam(this));
        this.goalSelector.addGoal(1, new SculkApostleSummonAbilityGoal(this));

        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 1, 3, 80, 150, 3));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistries.RIFT_WALKER.get(), 1, 3, 80, 150, 3));

        this.attackGoal = (ApostleOfSculkAnimatedWarlockAttackGoal) new ApostleOfSculkAnimatedWarlockAttackGoal(this, meleeSpeedModifier, 25, 40)
                .setMoveset(List.of(
                        new AttackAnimationData(50, "apostle_spear_attack", 14, 25),
                        new AttackAnimationData(20, "spin_slash_melee", 2, 5, 7, 10, 12, 15, 17, 20),
                        AttackAnimationData.builder("staff_right_spin_slice")
                                .length(55)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new AttackKeyframe(10, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(13, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new ApostleOfSculkBlockKeyframe(15),
                                        new AttackKeyframe(18, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(23, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(true, true)),
                                        new AttackKeyframe(40, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(42, new Vec3(0, 0.1, 1.85), new Vec3(0, .3, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(44, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(45, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(47, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(49, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(50, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(52, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true))
                                ).build(),
                        AttackAnimationData.builder("staff_upswing_slam_1")
                                .length(59)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(20, new Vec3(0, 0.25, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(44, new Vec3(0, 1, 0), new Vec3(0, 1.15, .1), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(48, new Vec3(0, -1, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(54, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build(),
                        AttackAnimationData.builder("apostle_spin_attack")
                                .length(87)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(22, new Vec3(0, 0.75, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(41, new Vec3(0, -0.75, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(45, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build()
                ))
                .setComboChance(1.0F)
                .setMeleeAttackInverval(25, 50)
                .setMeleeMovespeedModifier(meleeSpeedModifier)
                .setMeleeBias(0.50f, 0.80f)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHADOW_SLASH.get(),
                                SpellRegistries.ESOTERIC_EDGE.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistries.MEND_FLESH.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistries.RIFT_WALKER.get(),
                                SpellRegistry.SHADOW_SLASH.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.ESOTERIC_EDGE.get(), 70, 100, 3, 5)
                .setSpellQuality(2.0f, 2.0f);

        this.goalSelector.addGoal(3, attackGoal);

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.SHADOW_SLASH.get()
                ), 2.1f, 2.1f, 80, 150));

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.COUNTERSPELL_SPELL.get(),
                        SpellRegistries.ESOTERIC_EDGE.get()
                ), 2.1f, 2.1f, 50, 80));

        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Final?
    private void finalPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic + Melee
        this.goalSelector.addGoal(1, new SculkApostleAoECounterspellAbilityGoal(this));
        this.goalSelector.addGoal(1, new SculkApostleAoESculkSlam(this));
        this.goalSelector.addGoal(1, new SculkApostleSummonAbilityGoal(this));

        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 1, 3, 80, 150, 3));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistries.RIFT_WALKER.get(), 1, 3, 80, 150, 3));
        this.goalSelector.addGoal(2, new ApostleOfSculkAnimatedWarlockAttackGoal(this, meleeSpeedModifier, 25, 40)
                .setMoveset(List.of(
                        new AttackAnimationData(50, "apostle_spear_attack", 14, 25),
                        new AttackAnimationData(20, "spin_slash_melee", 2, 5, 7, 10, 12, 15, 17, 20),
                        AttackAnimationData.builder("staff_right_spin_slice")
                                .length(55)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new AttackKeyframe(10, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(13, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new ApostleOfSculkBlockKeyframe(15),
                                        new AttackKeyframe(18, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(23, new Vec3(0, 0, 0), new Vec3(0, .1, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(true, true)),
                                        new AttackKeyframe(40, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(42, new Vec3(0, 0.1, 1.85), new Vec3(0, .3, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(44, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(45, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(47, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(49, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true)),
                                        new AttackKeyframe(50, new Vec3(0, 0, 0)),
                                        new ApostleOfSculkAttackKeyframe(52, new Vec3(0, 0, 0), new Vec3(0, .05, 0.8), new ApostleOfSculkAttackKeyframe.SwingData(false, true))
                                ).build(),
                        AttackAnimationData.builder("staff_upswing_slam_1")
                                .length(59)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(20, new Vec3(0, 0.25, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(44, new Vec3(0, 1, 0), new Vec3(0, 1.15, .1), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(48, new Vec3(0, -1, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(54, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build(),
                        AttackAnimationData.builder("apostle_spin_attack")
                                .length(87)
                                .area(0.25f)
                                .rangeMultiplier(3f)
                                .attacks(
                                        new ApostleOfSculkAttackKeyframe(22, new Vec3(0, 0.75, 0), new Vec3(0, .1, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkAttackKeyframe(41, new Vec3(0, -0.75, 0), new Vec3(0, 0, 0), new ApostleOfSculkAttackKeyframe.SwingData(true, false)),
                                        new ApostleOfSculkHeavyAttackKeyframe(45, new Vec3(0, 0, -.2), new Vec3(0, 0, 0.5), new ApostleOfSculkHeavyAttackKeyframe.SwingData(true, true, true))
                                ).build()
                ))
                .setComboChance(1.5F)
                .setMeleeAttackInverval(10, 20)
                .setMeleeMovespeedModifier(1.5F)
                .setMeleeBias(0.80f, 0.90f)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHADOW_SLASH.get(),
                                SpellRegistries.ESOTERIC_EDGE.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistries.MEND_FLESH.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistries.RIFT_WALKER.get(),
                                SpellRegistry.SHADOW_SLASH.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.ESOTERIC_EDGE.get(), 70, 100, 3, 5)
                .setSpellQuality(2.0f, 2.0f)
        );

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.ELDRITCH_BLAST_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.SHADOW_SLASH.get()
                ), 2.1f, 2.1f, 80, 150));

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                        SpellRegistries.RIFT_WALKER.get(),
                        SpellRegistry.COUNTERSPELL_SPELL.get(),
                        SpellRegistries.ESOTERIC_EDGE.get()
                ), 2.1f, 2.1f, 50, 80));

        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public void procParry()
    {
        if (!level().isClientSide)
        {
            serverTriggerEvent(PROC_PARRY);
        }
        this.parryTime = 10;
    }

    public void procEnraged()
    {
        if (!level().isClientSide && getRageMeter() >= 5)
        {
            serverTriggerEvent(PROC_RAGE_QUIT);
            setEnraged(true);
        }
        // 30 seconds should be good enough?
        rageTime = 30 * 20;
    }

    public boolean activeParry()
    {
        return parryTime > 0;
    }

    // Tick stuff
    @Override
    public void tick() {
        super.tick();

        // Spawning
        if (isSpawning())
        {
            spawnTimer--;
            //hand spawn sequence
            handleSpawn();
        }

        // Handle our parries here
        if (parryTime > 0)
        {
            parryTime--;
        }
        if (parryCooldown > 0)
        {
            parryCooldown--;
        }
        if (rageTime > 0)
        {
            rageTime--;
            if (rageTime <= 0)
            {
                DiscerningTheEldritch.LOGGER.debug("Not angry anymore");
                setEnraged(false);
                setRageMeter(0);
                DiscerningTheEldritch.LOGGER.debug("Meter for Raw: " + getRageMeter());
                DiscerningTheEldritch.LOGGER.debug("Is RAAA: " + getEnraged());

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    if (player instanceof ServerPlayer serverPlayer)
                    {
                        // display a message to the player
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.enraged_notification")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))
                                .withStyle(ChatFormatting.BOLD)
                        ));
                    }
                }
            }
        }
        // Go on CD only after we're done being enraged
        if (rageCooldown > 0)
        {
            rageCooldown--;
            if (rageCooldown <= 0)
            {
                DiscerningTheEldritch.LOGGER.debug("Let's get angry again");
            }
        }

        // Handle our block-breaking here
        if (destroyBlockDelay > 0)
        {
            --destroyBlockDelay;
        }
        if (stuckDetectorDelay > 0)
        {
            --stuckDetectorDelay;
        }

        // These are used for getting health; very handy for doing phases based on health
        // Want to break these up into quarters
        float health = this.getHealth();
        float MAX_HEALTH = this.getMaxHealth();

        float halfHealth = MAX_HEALTH/2;
        float thirdHealth = MAX_HEALTH/3;
        float almostDead = MAX_HEALTH/4;

        // Once the boss is at half health or less, it will set the boss to its second phase
        // This will increase its spell power attribute, set its second goals
        // And set its health to its half health
        if (isPhase(Phase.FirstPhase))
        {
            if (this.getHealth() <= halfHealth)
            {
                int radius = 15;

                List<LivingEntity> entitiesNearby = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));

                // Used for displaying the taunt message to all players nearby who are fighting the boss
                for (LivingEntity targets : entitiesNearby)
                {
                    if (targets instanceof ServerPlayer player)
                    {
                        playSound(DTESoundRegistry.APOSTLE_OF_SCULK_TAUNT_ONE.get(), 1.5F, 1);
                        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.apostle_of_sculk_taunt_1")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0x1CE0E3)))));
                    }
                }

                setPhase(Phase.SecondPhase);

                if (!isDeadOrDying())
                {
                    setHealth(halfHealth);
                }

                secondPhaseGoals();

                this.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(1.75F);
                this.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).setBaseValue(1.55F);

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    lookAt(player, 360, 360);
                }
            }
        }
        // Second
        else if (isPhase(Phase.SecondPhase))
        {
            if (this.getHealth() <= almostDead)
            {
                //setInvulnerable(true);

                setPhase(Phase.ThirdPhase);

                if (!isDeadOrDying())
                {
                    setHealth(almostDead);
                }

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    // Just stare at the nearest player, aura farm this shit
                    lookAt(player, 360, 360);
                    // Play this ahead of time
                    playSound(DTESoundRegistry.APOSTLE_OF_SCULK_TAUNT_TWO.get(), 1.5F, 1);

                    //jumpBackwards(this, player);
                }
            }
        }
        // Transition
        else if (isPhase(Phase.ThirdPhase))
        {
            //setInvulnerable(true);

            if (--transitionAnimationTime <= 0)
            {
                int radius = 15;

                List<LivingEntity> entitiesNearby = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius));

                // Used for displaying the taunt message to all players nearby who are fighting the boss
                for (LivingEntity targets : entitiesNearby)
                {
                    if (targets instanceof ServerPlayer player)
                    {
                        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.apostle_of_sculk_taunt_2")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0x1CE0E3)))));
                    }
                }

                setPhase(Phase.FourthPhase);

                if (!isDeadOrDying())
                {
                    setHealth(almostDead);
                }

                finalPhaseGoals();

                this.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(1.85F);
                this.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).setBaseValue(1.75F);
                this.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(55);
                this.getAttributes().getInstance(Attributes.ARMOR).setBaseValue(65);

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    lookAt(player, 360, 360);
                }
            }
        }
        // Final
        else if (isPhase(Phase.FourthPhase))
        {
            setInvulnerable(false);

            // This "refills" the boss' health bar even though it is at almost dead health
            this.bossEvent.setProgress(health / (halfHealth - almostDead));
        }
    }

    private void handleSpawn()
    {
        int animProgress = spawnAnimTime + spawnDelay - spawnTimer;
        float crawl = getSpawnWalkPercent(0);
        if (!this.level().isClientSide && animProgress == 57)
        {
            this.serverTriggerEvent(START_MUSIC);
        }

        if (animProgress == spawnDelay)
        {
            if (!this.level().isClientSide)
            {
                serverTriggerAnimation("apostle_spawn");
            }
        }
    }

    // Looking at how Tyros does
    public float getSpawnWalkPercent(float partialTick)
    {
        return Math.clamp((spawnAnimTime - spawnTimer + partialTick) / (float) spawnAnimTime, 0, 1);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof IMagicSummon summon && summon.getSummoner() == this)
        {
            return true;
        }
        else if (entityIn.getType().is(DTETags.SCULK_ALLIES))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Hurt
    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return DTESoundRegistry.APOSTLE_OF_SCULK_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide)
        {
            return false;
        }

        // Parry
        // CD is based off phase, with the final few phases being the most likely to parry a lot
        boolean canParry = this.isAggressive() &&
                parryCooldown <= 0 &&
                !isImmobile() &&
                !attackGoal.isActing() &&
                source.getEntity() != null &&
                source.getSourcePosition() != null && source.getSourcePosition().subtract(this.position()).normalize().dot(this.getForward()) >= 0.35
                && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        if (canParry && random.nextFloat() < 0.5F)
        {
            if (isPhase(Phase.FourthPhase) || getEnraged())
            {
                DiscerningTheEldritch.LOGGER.debug("--PARRY A TON!!!--");
                serverTriggerAnimation("parry_deflect");
                procParry();
                this.parryCooldown = 60;
                this.playSound(SoundRegistry.FIRE_DAGGER_PARRY.get());
                if (source.getEntity() instanceof LivingEntity target)
                {
                    // Do kb on the entity to knock them back
                    knockbackAttacker(this, target);
                }

            } else
            {
                DiscerningTheEldritch.LOGGER.debug("--PARRY!!!--");
                serverTriggerAnimation("parry_deflect");
                procParry();
                this.parryCooldown = 100;
                this.playSound(SoundRegistry.FIRE_DAGGER_PARRY.get());
                if (source.getEntity() instanceof LivingEntity target)
                {
                    // Do kb on the entity to knock them back
                    knockbackAttacker(this, target);
                }

            }

            return false;
        }

        // Rage Mechanic
        boolean canRage =
                this.rageCooldown <= 0 &&
                this.rageTime <= 0 &&
                !isImmobile() &&
                !attackGoal.isActing() &&
                source.getEntity() != null;
        int rageMeter = getRageMeter();

        if (canRage)
        {
            // Per super-hurt hit, increment the rage meter
            if (amount >= DTEServerConfig.apostleOfSculkDamageCap)
            {
                rageMeter++;
                setRageMeter(rageMeter);
                DiscerningTheEldritch.LOGGER.debug("Rage: " + getRageMeter());
            }
            if (rageMeter >= 5 && !getEnraged())
            {
                serverTriggerAnimation("parry_deflect");
                procEnraged();
                rageCooldown = 60 * 20;
                this.playSound(SoundRegistry.BLACK_HOLE_CAST.get());

                DiscerningTheEldritch.LOGGER.debug("--RAGE!!!--");
                DiscerningTheEldritch.LOGGER.debug("Is Enraged? " + getEnraged());

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    if (source.getEntity() instanceof ServerPlayer serverPlayer)
                    {
                        // display a message to the player
                        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("display.discerning_the_eldritch.enraged_warning")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xF35F5F)))
                                .withStyle(ChatFormatting.BOLD)
                        ));
                    }
                }
            }
        }

        if (getEnraged() && amount < DTEServerConfig.apostleOfSculkDamageThreshold)
        {
            this.playSound(SoundRegistry.FIRE_DAGGER_PARRY.get());
            return false;
        }

        // Fuck you
        if (source.is(DamageTypeTags.IS_DROWNING)
                || source.is(DamageTypes.FALLING_STALACTITE)
                || source.is(DamageTypes.FALLING_ANVIL)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.CRAMMING)
        )
        {
            return false;
        }

        // We are going to also add in the damage cap here too for extra measure
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        {
            if (isTransitionPhase() || isSpawning())
            {
                return false;
            } else
            {
                // Break blocks if we're suffocating
                if ((source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CRAMMING)) && this.destroyBlockDelay <= 0)
                {
                    Utils.doMobBreakSuffocatingBlocks(this);
                    destroyBlockDelay = 40;
                }

                // This should hard clamp to our cap if something bypasses the event
                return super.hurt(source, ASUtils.basicDamageCap(amount, 0, DTEServerConfig.apostleOfSculkDamageCap));
                //return super.hurt(source, amount);
            }
        } else
        {
            // Break blocks if we're suffocating
            if ((source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CRAMMING)) && this.destroyBlockDelay <= 0)
            {
                Utils.doMobBreakSuffocatingBlocks(this);
                destroyBlockDelay = 40;
            }

            // This should hard clamp to our cap if something bypasses the event
            return super.hurt(source, amount);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        // Here too because I'm an ass
        //super.actuallyHurt(damageSource, ASUtils.basicDamageCap(damageAmount, 0, DTEServerConfig.apostleOfSculkDamageCap));
        super.actuallyHurt(damageSource, damageAmount);

        Vec3 oldStuckPos = this.lastStuck;
        this.lastStuck = this.position();
        if (stuckDetectorDelay <= 0) {
            if (oldStuckPos.distanceToSqr(lastStuck) < 3 * 3 && !isImmobile())
            {
                stuckDetectorDelay = 20;
                if (horizontalCollision)
                {
                    stuckDetector++;
                }
            } else
            {
                stuckDetector = 0;
            }
        }
        if (stuckDetector >= 3 && this.destroyBlockDelay <= 0)
        {
            Utils.doMobBreakSuffocatingBlocks(this, this.getForward().scale(1.5));
            stuckDetector = 0;
            destroyBlockDelay = 40;
        }
    }

    public static float healFor(float health, float percentage)
    {
        return health * (percentage / 100);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float MAX_HEALTH = this.getMaxHealth();
        if (super.doHurtTarget(entity))
        {
            if (getEnraged())
            {
                if (entity instanceof LivingEntity livingEntity)
                {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 3 * 20, 0, false, false, false));
                }
                this.heal(healFor(MAX_HEALTH, (float) DTEServerConfig.apostleOfSculkMeleeLifesteal + 0.5F));

                return true;
            }
            this.heal(healFor(MAX_HEALTH, (float) DTEServerConfig.apostleOfSculkMeleeLifesteal));

            return true;
        }

        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    private void knockbackAttacker(LivingEntity boss, LivingEntity target)
    {
        // Took this from Art of Forging, again
        // Getting target coords
        int xTarget = (int) boss.getX();
        int zTarget = (int) boss.getZ();
        // Getting attacker coords
        int xAttacker = (int) target.getX();
        int zAttacker = (int) target.getZ();

        // Normalize vec
        Vec3 vec3 = new Vec3(xAttacker, 0, zAttacker).subtract(xTarget, 0, zTarget).normalize();
        Vec3 vec3r = new Vec3(xTarget, 0, zTarget).subtract(xAttacker, 0, zAttacker).normalize();

        // Does the knockback
        target.push(vec3r.x, 0.2, vec3r.z);
    }

    // If she's gonna be in the air, she's not going to take fall damage
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    // She sculky
    @Override
    public boolean dampensVibrations() {
        return true;
    }

    // I'm so evil
    @Override
    public boolean canDisableShield() {
        if (isPhase(Phase.FourthPhase))
        {
            return true;
        }
        else
        {
            return super.canDisableShield();
        }
    }

    @Override
    public void kill() {
        if (this.isDeadOrDying() || this.isSpawning())
        {
            discard();
        }
        else {
            super.kill();
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        if (this.isDeadOrDying() && !this.level().isClientSide)
        {
            // Instead of doing the death sound on the death sound method, do it here
            // It's more cinematic when it's triggered by a player
            boolean deathByPlayer = this.lastHurtByPlayerTime > 0;
            if (deathByPlayer && this.lastHurtByPlayer != null)
            {
                playSound(DTESoundRegistry.APOSTLE_OF_SCULK_DEATH.get(), 1.5F, 1);
            }

            this.castComplete();
            this.serverTriggerAnimation("apostle_of_sculk_death");
            this.serverTriggerEvent(CLIENT_STOP_TRACKING);
            this.serverTriggerEvent(STOP_MUSIC);
        }
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        // Looking at how ISS does it for Tyros
        this.dropEquipment();
        this.dropExperience(damageSource.getEntity());

        boolean deathByPlayer = this.lastHurtByPlayerTime > 0;

        this.dropCustomDeathLoot(level, damageSource, deathByPlayer);

        ResourceKey<LootTable> lootTable = this.getLootTable();
        LootTable mainLoot = this.level().getServer().reloadableRegistries().getLootTable(lootTable);
        LootTable tormentModeLoot = this.level().getServer().reloadableRegistries().getLootTable(ResourceKey.create(lootTable.registryKey(), lootTable.location().withSuffix("_torment_mode")));

        LootParams.Builder builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());

        if (deathByPlayer && this.lastHurtByPlayer != null)
        {
            builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer)
                    .withLuck(this.lastHurtByPlayer.getLuck());
        }

        LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
        ObjectArrayList<ItemStack> objectArrayList = new ObjectArrayList<>();
        mainLoot.getRandomItems(lootParams, this.getLootTableSeed(), objectArrayList::add);

        if (ASUtils.hasCurio(lastHurtByPlayer, ItemRegistries.TORMENT_NEXUS.get()))
        {
            tormentModeLoot.getRandomItems(lootParams, this.getLootTableSeed(), objectArrayList::add);
        }
        this.deathLoot = new SimpleContainer(objectArrayList.size());
        objectArrayList.forEach(deathLoot::addItem);
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        if (!level().isClientSide)
        {
            if (this.deathTime >= deathAnimationTime && !this.level().isClientSide() && !this.isRemoved())
            {
                if (this.deathLoot != null)
                {
                    deathLoot.getItems().forEach(this::spawnAtLocation);
                }
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    // Puts items on the boss like armors and weapons
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.SCULK_APOSTLE_HOOD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.SCULK_APOSTLE_ROBES.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.SCULK_APOSTLE_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.SCULK_APOSTLE_GREAVES.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistries.STAFF_OF_THE_SPECTRE.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    // Creates the entity attributes for the boss
    public static AttributeSupplier.Builder createAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 16.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MAX_HEALTH, 1000.0)
                .add(Attributes.ARMOR, 55)
                .add(Attributes.ARMOR_TOUGHNESS, 45)
                .add(Attributes.FOLLOW_RANGE, 85.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 4.5)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(AttributeRegistry.SPELL_POWER, 1.6)
                .add(AttributeRegistry.SPELL_RESIST, 1.65)
                .add(AttributeRegistry.MAX_MANA, 1400)
                .add(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.25)
                .add(ASAttributeRegistry.MANA_REND, 0.25)
                .add(Attributes.SCALE, 1.30)
                ;
    }

    // Setters & Getters
    // Phases
    @Override
    public void setPhase(int phase) {
        this.entityData.set(PHASE, phase);
    }

    @Override
    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    // Enraged
    public void setEnraged(boolean rage) {
        this.entityData.set(ENRAGED, rage);
    }

    public boolean getEnraged() {
        return this.entityData.get(ENRAGED);
    }

    // Rage Meter
    public void setRageMeter(int rage) {
        this.entityData.set(RAGE_METER, rage);
    }

    public int getRageMeter() {
        return this.entityData.get(RAGE_METER);
    }

    // Torment Mode
    public void setTormentMode(boolean tormentMode) {
        this.entityData.set(TORMENT_MODE, tormentMode);
    }

    public boolean getTormentMode() {
        return this.entityData.get(TORMENT_MODE);
    }

    // NBT
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // Phases
        pCompound.putInt("phase", getPhase());
        // Rage
        pCompound.putInt("rage_meter", getRageMeter());
        pCompound.putBoolean("is_enraged", getEnraged());
        // Torment Mode
        pCompound.putBoolean("is_torment_mode", getTormentMode());

        if (rageTime > 0)
        {
            pCompound.putInt("rage_timer", rageTime);
        }
        if (rageCooldown > 0)
        {
            pCompound.putInt("rage_cooldown", rageCooldown);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.hasCustomName())
        {
            this.bossEvent.setName(this.getDisplayName());
        }
        // Phases
        setPhase(pCompound.getInt("phase"));
        if (isPhase(Phase.SecondPhase))
        {
            secondPhaseGoals();
        }
        if (isPhase(Phase.FourthPhase))
        {
            finalPhaseGoals();
        }
        // Loot
        if (deathLoot != null)
        {
            pCompound.put("deathLootItems", deathLoot.createTag(this.registryAccess()));
        }
        // Loot
        if (pCompound.contains("deathLootItems", 9))
        {
            var tag = pCompound.getList("deathLootItems", 10);
            this.deathLoot = new SimpleContainer(tag.size());
            this.deathLoot.fromTag(tag, this.registryAccess());
        }

        // Boss Bar
        if (this.hasCustomName())
        {
            this.bossEvent.setName(this.getDisplayName());
        }

        // Rage
        setRageMeter(pCompound.getInt("rage_meter"));
        setEnraged(pCompound.getBoolean("is_enraged"));

        int rageTime = pCompound.getInt("rage_timer");
        if (rageTime > 0)
        {
            this.rageTime = rageTime;
        }

        int rageCooldown = pCompound.getInt("rage_cooldown");
        if (rageCooldown > 0)
        {
            this.rageCooldown = rageCooldown;
        }

        // Torment Mode
        setTormentMode(pCompound.getBoolean("is_torment_mode"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PHASE, 0);
        pBuilder.define(ENRAGED, false);
        pBuilder.define(RAGE_METER, 0);
        pBuilder.define(TORMENT_MODE, false);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (!this.level().isClientSide)
        {
            createBossEvent();
        }
    }

    protected void createBossEvent()
    {
        this.bossEvent = (ExtendedServerBossEvent) (new ExtendedServerBossEvent(this.getUUID(), this.getDisplayName().copy().withStyle(ChatFormatting.DARK_AQUA/*, ChatFormatting.BOLD*/), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setCreateWorldFog(false);
    }

    @Override
    protected boolean isImmobile() {
        return isPhase(Phase.ThirdPhase) || isSpawning() || super.isImmobile();
    }

    @Override
    public boolean isPushable() {
        return !isTransitionPhase() || !isSpawning();
    }

    @Override
    public void calculateEntityAnimation(boolean includeHeight) {
        super.calculateEntityAnimation(false);
    }

    @Override
    public boolean bobBodyWhileWalking() {
        return !isAnimating();
    }

    /***
     * Geckolib anims
     */
    private final RawAnimation transitionPhaseAnimation = RawAnimation.begin().thenPlay("apostle_of_sculk_transition");
    private final RawAnimation deathAnimation = RawAnimation.begin().thenPlay("apostle_of_sculk_death");

    private final AnimationController<ApostleOfSculkBoss> transitionController = new AnimationController<>(this, "ascended_one_transition", 0, this::transitionPredicate);
    private final AnimationController<ApostleOfSculkBoss> deathController = new AnimationController<>(this, "ascended_one_death", 0, this::deathPredicate);
    private final AnimationController<ApostleOfSculkBoss> meleeController = new AnimationController<>(this, "keeper_animations", 0, this::meleePredicate);

    RawAnimation animationToPlay = null;
    boolean animPriority;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(transitionController);
        controllerRegistrar.add(deathController);
        controllerRegistrar.add(meleeController);
        super.registerControllers(controllerRegistrar);
    }

    private PlayState transitionPredicate(AnimationState<ApostleOfSculkBoss> animationState)
    {
        var controller = animationState.getController();
        if (isTransitionPhase())
        {
            controller.setAnimation(transitionPhaseAnimation);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState deathPredicate(AnimationState<ApostleOfSculkBoss> animationState)
    {
        var controller = animationState.getController();
        if (this.isDeadOrDying())
        {
            controller.setAnimation(deathAnimation);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState meleePredicate(AnimationState<ApostleOfSculkBoss> animationState)
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

    public boolean isTransitionPhase()
    {
        return isPhase(Phase.ThirdPhase);
    }

    public boolean isSpawning()
    {
        return spawnTimer > 0;
    }

    public void triggerSpawnAnim()
    {
        this.spawnTimer = spawnAnimTime + spawnDelay;
    }

    @Override
    public void playAnimation(String animationId) {
        animationToPlay = RawAnimation.begin().thenPlay(animationId);
        animPriority = animationId.equals("apostle_spawn");
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return !isTransitionPhase() || this.isDeadOrDying();
    }

    @Override
    public boolean isAnimating() {
        return meleeController.getAnimationState() != AnimationController.State.STOPPED && !animPriority || super.isAnimating();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeInt(this.spawnTimer);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.spawnTimer = registryFriendlyByteBuf.readInt();
        float y = this.getYRot();
        this.yBodyRot = y;
        this.yBodyRotO = y;
        this.yHeadRot = y;
        this.yHeadRotO = y;
        this.yRotO = y;
    }
}
