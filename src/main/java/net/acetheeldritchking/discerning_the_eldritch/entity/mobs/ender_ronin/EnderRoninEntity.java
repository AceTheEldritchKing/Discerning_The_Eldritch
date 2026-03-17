package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.ender_ronin;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.FocusOnTradingPlayerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.acetheeldritchking.aces_spell_utils.entity.mobs.goals.WizardSpellComboGoal;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.goals.ender_ronin.EnderRoninAnimatedWarlockAttackGoal;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.*;

public class EnderRoninEntity extends NeutralWizard implements IMerchantWizard, IAnimatedAttacker {
    public EnderRoninEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 45;
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
        this.goalSelector.addGoal(0, new FocusOnTradingPlayerGoal<>(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SpellBarrageGoal(this, SpellRegistry.MAGIC_MISSILE_SPELL.get(), 3, 5, 100, 250, 6));
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 35, .75f));

        this.goalSelector.addGoal(3, new EnderRoninAnimatedWarlockAttackGoal(this, 1.5f, 25, 30)
                .setMoveset(List.of(
                        new AttackAnimationData(9, "simple_sword_upward_swipe", 5),
                        new AttackAnimationData(10, "simple_sword_horizontal_cross_swipe", 8),
                        new AttackAnimationData(8, "katana_upslash", 2)
                ))
                .setComboChance(0.3F)
                .setMeleeAttackInverval(15, 25)
                .setMeleeMovespeedModifier(1.5F)
                .setMeleeBias(0.1f, 0.25f)
                .setSpells(
                        // Attack
                        List.of(SpellRegistry.MAGIC_ARROW_SPELL.get(), SpellRegistry.ECHOING_STRIKES_SPELL.get(), SpellRegistry.SHADOW_SLASH.get(), SpellRegistry.MAGIC_MISSILE_SPELL.get()),
                        // Defense
                        List.of(SpellRegistry.COUNTERSPELL_SPELL.get(), SpellRegistry.HEAL_SPELL.get(), SpellRegistry.SUMMON_SWORDS.get(), SpellRegistry.EVASION_SPELL.get()),
                        // Movement
                        List.of(SpellRegistry.TELEPORT_SPELL.get(), SpellRegistry.COUNTERSPELL_SPELL.get()),
                        // Support
                        List.of(SpellRegistry.ECHOING_STRIKES_SPELL.get())
                ).setSingleUseSpell(SpellRegistry.BLACK_HOLE_SPELL.get(), 700, 1000, 5, 5)
                .setSpellQuality(1.0f, 1.0f)
                .setDrinksPotions()
        );

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.TELEPORT_SPELL.get(),
                        SpellRegistry.COUNTERSPELL_SPELL.get(),
                        SpellRegistry.DRAGON_BREATH_SPELL.get()
                ), 1.0f, 1.0f, 100, 250));

        this.goalSelector.addGoal(2, new WizardSpellComboGoal(this,
                List.of(
                        SpellRegistry.SHADOW_SLASH.get(),
                        SpellRegistry.SHADOW_SLASH.get(),
                        SpellRegistry.SHADOW_SLASH.get()
                ), 1.0f, 1.0f, 150, 250));

        this.goalSelector.addGoal(4, new PatrolNearLocationGoal(this, 30, .75f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        // They HATE these guys  - But not YOU
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Endermite.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, KeeperEntity.class, true));

        // Ok maybe they hate you if you steal their shit
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        //this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.STARVOID_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.STARVOID_CUIRASS.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.STARVOID_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.STARVOID_GREAVES.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistries.STARMETAL_ODACHI.get()));
        //this.setDropChance(EquipmentSlot.HEAD, 0.0F);
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

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5.5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 5)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.MAX_HEALTH, 65.0)
                .add(Attributes.FOLLOW_RANGE, 45.0)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.5)
                .add(Attributes.MOVEMENT_SPEED, .3)
                .add(AttributeRegistry.MAX_MANA, 500)
                .add(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.05)
                ;
    }

    @Override
    public boolean shouldSheathSword() {
        return true;
    }

    /***
     * Merchant
     */
    @Nullable
    private Player tradingPartner;
    @Nullable
    protected MerchantOffers offer;

    // Serialized
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    // Not Serialized
    private long lastRestockCheckDayTime;

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean preventTrade = (!this.level().isClientSide && this.getOffers().isEmpty() || this.getTarget() != null || isAngryAt(player));

        if (hand == InteractionHand.MAIN_HAND)
        {
            if (preventTrade && !this.level().isClientSide)
            {
                // Iron doesn't have anything in here, so I don't think I should either
            }
        }
        if (!preventTrade)
        {
            if (!this.level().isClientSide && !this.getOffers().isEmpty())
            {
                if (shouldRestock())
                {
                    restock();
                }
                this.startTrading(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private void startTrading(Player player)
    {
        this.setTradingPlayer(player);
        this.lookControl.setLookAt(player);
        this.openTradingScreen(player, this.getDisplayName(), 0);
    }

    @Override
    public int getRestocksToday() {
        return numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocks) {
        this.numberOfRestocksToday = restocks;
    }

    @Override
    public long getLastRestockGameTime() {
        return lastRestockGameTime;
    }

    @Override
    public void setLastRestockGameTime(long time) {
        this.lastRestockGameTime = time;
    }

    @Override
    public long getLastRestockCheckDayTime() {
        return lastRestockCheckDayTime;
    }

    @Override
    public void setLastRestockCheckDayTime(long time) {
        this.lastRestockCheckDayTime = time;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPartner = player;
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return tradingPartner;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offer == null)
        {
            this.offer = new MerchantOffers();

            this.offer.addAll(createRandomOffers(2, 3));

            // Ink
            if (this.random.nextFloat() < 0.25f) {
                this.offer.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offer.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_RARE.get()).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offer.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_EPIC.get()).getOffer(this, this.random));
            }

            this.offer.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ENDER.get()), 0F, 0.25F).getOffer(this, this.random));
            if (this.random.nextFloat() < .8f) {
                this.offer.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ENDER.get()), .3f, .7f).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < .8f) {
                this.offer.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ENDER.get()), .8f, 1f).getOffer(this, this.random));
            }

            this.offer.add(new MerchantOffer(
                    new ItemCost(ItemRegistry.DRAGONSKIN_SPELL_BOOK.get(), 1),
                    new ItemStack(ItemRegistries.STARSTRUCK_SHEATH.get(), 1),
                    2,
                    0,
                    1.2F
            ));

            this.offer.addAll(createRandomStagOffers(2, 4));

            this.offer.removeIf(Objects::isNull);

            numberOfRestocksToday++;
        }

        return this.offer;
    }

    private static final List<VillagerTrades.ItemListing> filler = List.of(
            new AdditionalWanderingTrades.SimpleBuy(1, new ItemCost(Items.DRAGON_BREATH, 1), 6, 12),
            new AdditionalWanderingTrades.SimpleSell(1, new ItemStack(ItemRegistry.ENDER_RUNE.get(), 2), 25, 55),
            new AdditionalWanderingTrades.SimpleSell(1, new ItemStack(ItemRegistry.AMETHYST_RAPIER.get(), 1), 34, 64),
            new AdditionalWanderingTrades.SimpleSell(1, new ItemStack(ItemRegistry.EVASION_ELIXIR.get(), 1), 2, 5),
            new AdditionalWanderingTrades.SimpleSell(1, new ItemStack(ItemRegistries.STARSTONE.get(), 5), 5, 10)
    );

    private static final List<MerchantOffer> stagTrades = List.of(
            new MerchantOffer(
                    new ItemCost(ItemRegistries.STARSTONE.get(), 16),
                    new ItemStack(ItemRegistries.STARMETAL_SCYTHE.get(), 1),
                    1,
                    0,
                    1.0F
            ),
            new MerchantOffer(
                    new ItemCost(ItemRegistries.STARSTONE.get(), 16),
                    new ItemStack(ItemRegistries.STARMETAL_SCYTHE.get(), 1),
                    1,
                    0,
                    1.0F
            ),
            new MerchantOffer(
                    new ItemCost(ItemRegistries.STARMETAL_INGOT.get(), 16),
                    new ItemStack(ItemRegistries.VOIDSPLITTER_SCYTHE.get(), 1),
                    1,
                    0,
                    1.0F
            )
    );

    private Collection<MerchantOffer> createRandomOffers(int min, int max)
    {
        Set<Integer> set = Sets.newHashSet();
        int fillerTrades = random.nextIntBetweenInclusive(min, max);
        for (int i = 0; i < 10 && set.size() < fillerTrades; i++)
        {
            set.add(random.nextInt(filler.size()));
        }
        Collection<MerchantOffer> offers = new ArrayList<>();
        for (Integer integer : set)
        {
            offers.add(filler.get(integer).getOffer(this, this.random));
        }

        return offers;
    }

    private Collection<MerchantOffer> createRandomStagOffers(int min, int max)
    {
        Set<Integer> set = Sets.newHashSet();
        int fillerTrades = random.nextIntBetweenInclusive(min, max);
        for (int i = 0; i < 10 && set.size() < fillerTrades; i++)
        {
            set.add(random.nextInt(stagTrades.size()));
        }
        Collection<MerchantOffer> offers = new ArrayList<>();
        for (Integer integer : set)
        {
            offers.add(stagTrades.get(integer));
        }

        return offers;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {
        // Nothing in here
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isTrading();
    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {
        merchantOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {
        if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20)
        {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!itemStack.isEmpty()), this.getSoundVolume(), this.getSoundVolume());
        }
    }

    protected  SoundEvent getTradeUpdatedSound(boolean affirm)
    {
        return affirm ? SoundRegistry.TRADER_YES.get() : SoundRegistry.TRADER_NO.get();
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundRegistry.TRADER_YES.get();
    }

    RawAnimation animationToPlay = null;
    private final AnimationController<EnderRoninEntity> meleeController = new AnimationController<>(this, "keeper_animations", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public void playAnimation(String animationId) {
        animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState predicate(AnimationState<EnderRoninEntity> animationState)
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

    /***
     * NBT
     */
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        serializeMerchant(pCompound, this.offer, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        deserializeMerchant(pCompound, c -> this.offer = c);
    }
}
