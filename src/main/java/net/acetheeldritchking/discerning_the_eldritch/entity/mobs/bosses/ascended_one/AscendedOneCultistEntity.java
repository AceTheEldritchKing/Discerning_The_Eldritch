package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.ascended_one;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;

public class AscendedOneCultistEntity extends AbstractSpellCastingMob {
    private final static EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(AscendedOneCultistEntity.class, EntityDataSerializers.BOOLEAN);
    private int currentAnimTime;
    private final int animLength = (int) (20 * 2f);

    public AscendedOneCultistEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setPersistenceRequired();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!triggered()) {
            trigger();
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    private void trigger()
    {
        if (!triggered())
        {
            this.entityData.set(TRIGGERED, true);
        }
    }

    public boolean triggered()
    {
        return this.entityData.get(TRIGGERED);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        {
            discard();
            return true;
        }
        else {
            Player player = level().getNearestPlayer(this, 8);
            if (player != null)
            {
                trigger();
            }
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (triggered()) {
            ++currentAnimTime;
            if (!level().isClientSide) {
                if (currentAnimTime > animLength) {
                    AscendedOneBoss ascendedOneBoss = new AscendedOneBoss(level());
                    ascendedOneBoss.moveTo(this.position());
                    ascendedOneBoss.finalizeSpawn((ServerLevelAccessor) level(), level().getCurrentDifficultyAt(ascendedOneBoss.getOnPos()), MobSpawnType.TRIGGERED, null);

                    ascendedOneBoss.setPersistenceRequired();

                    level().addFreshEntity(ascendedOneBoss);

                    discard();
                }
            }
        }
    }

    // Puts items on the boss like armors and weapons
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistries.ASCENDED_ONE_HOOD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistries.CAPELESS_ASCENDED_ONE_ROBES.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistries.ASCENDED_ONE_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistries.ASCENDED_ONE_GREAVES.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    // Geckolib
    private final RawAnimation idle = RawAnimation.begin().thenLoop("ascended_pose1");
    private final RawAnimation rise = RawAnimation.begin().thenPlay("ascended_pose2");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idlePredicate));
    }

    private PlayState idlePredicate(AnimationState<AscendedOneCultistEntity> event)
    {
        if (triggered())
        {
            event.getController().setAnimation(rise);
        }
        else {
            event.getController().setAnimation(idle);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return false;
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return false;
    }

    // NBT
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(TRIGGERED, false);
    }
}
