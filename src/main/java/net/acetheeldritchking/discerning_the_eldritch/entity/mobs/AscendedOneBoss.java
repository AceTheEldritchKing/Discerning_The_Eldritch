package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.boss_music.BossMusicManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;

import java.util.List;

public class AscendedOneBoss extends GenericBossEntity implements IAnimatedAttacker {
    // This method is only needed if you plan on summoning the boss with a spell
    public AscendedOneBoss(Level level)
    {
        this(DTEEntityRegistry.ASCENDED_ONE.get(), level);
        setPersistenceRequired();
    }

    // Constructor for the boss
    public AscendedOneBoss(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setPersistenceRequired();
        xpReward = 60;
        this.lookControl = createLookControl();
        this.moveControl = createMoveControl();
    }

    // These are used for doing boss bars, setting up the phase serializer for NBT, and stopping and starting music
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.translatable("bossbar.discerning_the_eldritch.ascended_one_boss"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final static EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AscendedOneBoss.class, EntityDataSerializers.INT);
    public static final byte STOP_MUSIC = 0;
    public static final byte START_MUSIC = 1;

    // Boss music
    public static SoundEvent bossMusic = DTESoundRegistry.TEST_BOSS_MUSIC.get();

    // Animation ticks
    public int transitionAnimationTime = 73;
    public int deathAnimationTime = 55;

    // Loot
    SimpleContainer deathLoot = null;

    @Override
    public SoundEvent getBossMusic() {
        return bossMusic;
    }

    // Helps handle the starting and stopping of boss music
    @Override
    public void handleClientEvent(byte eventId)
    {
        switch (eventId)
        {
            case STOP_MUSIC -> BossMusicManager.stop(this);
            case START_MUSIC -> BossMusicManager.createOrResumeInstance(this);
        }
    }

    // These two methods add and remove the boss bar and music based on how far the player is/if it is seen by the boss
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

    // Register the basic goals for the boss
    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        firstPhaseGoals();
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // First phase spells
    private void firstPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 80, 150, 1));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 20, 35)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHOCKWAVE_SPELL.get(),
                                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                                SpellRegistry.BLOOD_NEEDLES_SPELL.get(),
                                SpellRegistry.FIRE_ARROW_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.SUMMON_SWORDS.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.THUNDERSTORM_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.BLOOD_STEP_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.RAISE_DEAD_SPELL.get(),
                                SpellRegistry.SUMMON_VEX_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get()
                        )
                ).setSingleUseSpell(SpellRegistries.BOOGIE_WOOGIE.get(), 70, 100, 3, 5)
                .setSpellQuality(1.2f, 1.2f)
                .setDrinksPotions());
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Second phase spells
    private void secondPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 50, 80, 3));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 20, 35)
                .setSpells(
                        // Attack
                        List.of(
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                SpellRegistry.SHOCKWAVE_SPELL.get(),
                                SpellRegistry.BLOOD_SLASH_SPELL.get(),
                                SpellRegistry.BLOOD_NEEDLES_SPELL.get(),
                                SpellRegistry.ACUPUNCTURE_SPELL.get(),
                                SpellRegistry.FIRE_ARROW_SPELL.get(),
                                SpellRegistry.MAGIC_ARROW_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                                SpellRegistry.SUMMON_SWORDS.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.BLIGHT_SPELL.get(),
                                SpellRegistry.THUNDERSTORM_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.BLOOD_STEP_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.RAISE_DEAD_SPELL.get(),
                                SpellRegistry.SUMMON_VEX_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get(),
                                SpellRegistries.CONJURE_FORSAKE_AID.get()
                        )
                ).setSingleUseSpell(SpellRegistries.CONJURE_FORSAKE_AID.get(), 70, 100, 3, 5)
                .setSpellQuality(1.3f, 1.3f)
                .setDrinksPotions());
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Final phase spells
    private void finalPhaseGoals()
    {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals((x) -> true);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Magic Spells
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 5, 5, 30, 50, 5));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.ABYSSAL_SHROUD_SPELL.get(), 1, 3, 80, 100, 0));
        this.goalSelector.addGoal(3, new WizardAttackGoal(this, 1.25f, 10, 25)
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
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.FIRE_ARROW_SPELL.get(),
                                SpellRegistry.MAGIC_ARROW_SPELL.get(),
                                SpellRegistry.SUMMON_SWORDS.get(),
                                SpellRegistry.SACRIFICE_SPELL.get(),
                                SpellRegistries.CONJURE_FORSAKE_AID.get()
                        ),
                        // Defense
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.BLIGHT_SPELL.get(),
                                SpellRegistry.SLOW_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.BLOOD_STEP_SPELL.get(),
                                SpellRegistries.CONJURE_FORSAKE_AID.get(),
                                SpellRegistry.THUNDERSTORM_SPELL.get()
                        ),
                        // Movement
                        List.of(
                                SpellRegistry.BLOOD_STEP_SPELL.get(),
                                SpellRegistry.PLANAR_SIGHT_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get()
                        ),
                        // Support
                        List.of(
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.SUMMON_VEX_SPELL.get(),
                                SpellRegistries.CONJURE_FORSAKE_AID.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.SACRIFICE_SPELL.get()
                        )
                        // Silence down here is a temp thing
                ).setSingleUseSpell(SpellRegistries.SILENCE.get(), 100, 250, 5, 5)
                .setSpellQuality(1.5f, 1.5f));
        this.goalSelector.addGoal(5, new PatrolNearLocationGoal(this, 32.0F, 0.9));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    // Down here in the tick event we do more keeping track of the boss
    // For setting the boss bar and the boss phases
    @Override
    public void tick() {
        super.tick();

        // These are used for getting health; very handy for doing phases based on health
        float health = this.getHealth();
        float MAX_HEALTH = this.getMaxHealth();
        float healthPercentage = (health/MAX_HEALTH) * 100;

        float halfHealth = MAX_HEALTH/2;
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
                        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.ascended_one_taunt_1")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xC71B8B)))));
                    }
                }

                setPhase(Phase.SecondPhase);

                if (!isDeadOrDying())
                {
                    setHealth(halfHealth);
                }

                secondPhaseGoals();

                this.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(1.5F);
                this.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).setBaseValue(1.2F);

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    lookAt(player, 360, 360);

                    jumpBackwards(this, player);
                }
            }
        }
        // Second
        else if (isPhase(Phase.SecondPhase))
        {
            if (this.getHealth() <= almostDead)
            {
                //setInvulnerable(true);

                setPhase(Phase.TransitionPhase1);

                if (!isDeadOrDying())
                {
                    setHealth(almostDead);
                }

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    lookAt(player, 360, 360);

                    jumpBackwards(this, player);
                }
            }
        }
        // Transition
        else if (isPhase(Phase.TransitionPhase1))
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
                        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("display.discerning_the_eldritch.ascended_one_taunt_2")
                                .withStyle(s -> s.withColor(TextColor.fromRgb(0xC71B8B)))));
                    }
                }

                setPhase(Phase.DesperationPhase);
                setHealth(halfHealth);

                finalPhaseGoals();

                this.getAttributes().getInstance(AttributeRegistry.SPELL_POWER).setBaseValue(2F);
                this.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).setBaseValue(1.3F);

                var player = level().getNearestPlayer(this, 16);
                if (player != null)
                {
                    lookAt(player, 360, 360);

                    jumpBackwards(this, player);
                }
            }
        }
        // Final
        else if (isPhase(Phase.DesperationPhase))
        {
            setInvulnerable(false);

            // This "refills" the boss' health bar even though it is at half health
            this.bossEvent.setProgress(health / (MAX_HEALTH - almostDead));
        }
    }

    private void jumpBackwards(LivingEntity entity, LivingEntity player)
    {
        // Took this from Art of Forging
        this.isJumping = true;

        // Getting target coords
        int xTarget = (int) entity.getX();
        int zTarget = (int) entity.getZ();
        // Getting attacker coords
        int xAttacker = (int) player.getX();
        int zAttacker = (int) player.getZ();

        // Normalize vec
        Vec3 vec3 = new Vec3(xAttacker, 0, zAttacker).subtract(xTarget, 0, zTarget).normalize();
        Vec3 vec3r = new Vec3(xTarget, 0, zTarget).subtract(xAttacker, 0, zAttacker).normalize();

        // Does the knockback
        entity.push(vec3r.x, 0.5, vec3r.z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isTransitionPhase() || isJumpingBack())
        {
            // Prevent any damage if he's in his transition phase or jumping backwards
            return false;
        }
        else {
            return super.hurt(source, amount);
        }
    }

    @Override
    public void kill() {
        if (this.isDeadOrDying())
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
            this.castComplete();
            this.serverTriggerAnimation("ascended_death");
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

    // Used for determining which mobs the boss is allied to
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

    // Puts items on the boss like armors and weapons
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.ASCENDED_ONE_HOOD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.CAPELESS_ASCENDED_ONE_ROBES.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.ASCENDED_ONE_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.ASCENDED_ONE_GREAVES.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistries.STAFF_OF_ASCENSION.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemRegistries.BLACK_BOOK_SPELLBOOK.get()));
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
                .add(Attributes.ATTACK_DAMAGE, 15.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.5)
                .add(Attributes.MAX_HEALTH, 550.0)
                .add(Attributes.ARMOR, 60)
                .add(Attributes.ARMOR_TOUGHNESS, 20)
                .add(Attributes.FOLLOW_RANGE, 80.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 4.0)
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
        if (pCompound.contains("deathLootItems", 9))
        {
            var tag = pCompound.getList("deathLootItems", 10);
            this.deathLoot = new SimpleContainer(tag.size());
            this.deathLoot.fromTag(tag, this.registryAccess());
        }
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
        if (isPhase(Phase.FinalPhase))
        {
            finalPhaseGoals();
        }
        if (deathLoot != null)
        {
            pCompound.put("deathLootItems", deathLoot.createTag(this.registryAccess()));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PHASE, 0);
    }

    @Override
    protected boolean isImmobile() {
        return isPhase(Phase.TransitionPhase1) || super.isImmobile();
    }

    @Override
    public boolean isPushable() {
        return !isTransitionPhase();
    }

    /***
     * Geckolib anims
     */
    private final RawAnimation transitionPhaseAnimation = RawAnimation.begin().thenPlay("ascended_desperation");
    private final RawAnimation deathAnimation = RawAnimation.begin().thenPlay("ascended_death");
    private final RawAnimation jumpAnimation = RawAnimation.begin().thenPlay("ascended_jump");

    private final AnimationController<AscendedOneBoss> transitionController = new AnimationController<>(this, "ascended_one_transition", 0, this::transitionPredicate);
    private final AnimationController<AscendedOneBoss> deathController = new AnimationController<>(this, "ascended_one_death", 0, this::deathPredicate);
    private final AnimationController<AscendedOneBoss> jumpController = new AnimationController<>(this, "ascended_one_jump", 0, this::jumpPredicate);

    RawAnimation animationToPlay = null;

    public boolean isJumping;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(transitionController);
        controllerRegistrar.add(deathController);
        controllerRegistrar.add(jumpController);
        super.registerControllers(controllerRegistrar);
    }

    private PlayState transitionPredicate(AnimationState<AscendedOneBoss> animationState)
    {
        var controller = animationState.getController();
        if (isTransitionPhase())
        {
            controller.setAnimation(transitionPhaseAnimation);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState deathPredicate(AnimationState<AscendedOneBoss> animationState)
    {
        var controller = animationState.getController();
        if (this.isDeadOrDying())
        {
            controller.setAnimation(deathAnimation);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private PlayState jumpPredicate(AnimationState<AscendedOneBoss> animationState)
    {
        var controller = animationState.getController();
        if (isJumpingBack() == true)
        {
            controller.forceAnimationReset();
            controller.setAnimation(jumpAnimation);

            this.isJumping = false;
        }

        return transitionController.getAnimationState() == AnimationController.State.STOPPED ? PlayState.CONTINUE : PlayState.STOP;
    }

    public boolean isTransitionPhase()
    {
        return isPhase(Phase.TransitionPhase1);
    }

    public boolean isJumpingBack()
    {
        return this.isJumping;
    }

    @Override
    public void playAnimation(String animationId) {
        animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return !isTransitionPhase() || this.isDeadOrDying();
    }
}
