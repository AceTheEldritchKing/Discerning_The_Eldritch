package net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDamageTypes;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEEntityRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Collections;
import java.util.UUID;

public class RazorBlade extends LivingEntity implements GeoEntity, AntiMagicSusceptible, GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RazorBlade(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float damage;
    private int age;
    private DamageSource damageSource;

    public RazorBlade(Level level, LivingEntity owner, float damage) {
        this(DTEEntityRegistry.RAZOR_BLADE.get(), level);
        setOwner(owner);
        setDamage(damage);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        //
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void tick() {
        if (!level().isClientSide)
        {
            if (age > 150)
            {
                this.discard();
            } else
            {
                if (age < 1)
                {
                    playSound(DTESoundRegistry.RAZOR_UNSHEATHED.get(), 1, 1);
                }
                if (age < 130 && (age) % 20 == 0)
                {
                    level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.1)).forEach(this::dealDamage);
                }
            }
            if (age == 100 && Utils.random.nextFloat() < 0.4F)
            {
                playSound(DTESoundRegistry.RAZOR_SHEATHED.get(), 1, 1);
            }
        }

        age++;
    }

    public boolean dealDamage(LivingEntity target)
    {
        if (damageSource == null) {
            // Temp ISS stuff
            damageSource = new DamageSource(DamageSources.getHolderFromResource(target, DTEDamageTypes.RAZOR_DAMAGE), this, getOwner());
        }

        if (target != getOwner())
        {
            if (DamageSources.applyDamage(target, damage, damageSource))
            {
                target.invulnerableTime = 0;
                return true;
            }
        }
        return false;
    }

    public void setOwner(@Nullable LivingEntity owner)
    {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    public LivingEntity getOwner()
    {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel)
        {
            Entity entity = serverLevel.getEntity(this.ownerUUID);

            if (entity instanceof LivingEntity livingEntity)
            {
                this.owner = livingEntity;
            }
        }

        return this.owner;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        //
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    // NBT
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.age = compound.getInt("Age");
        if (compound.hasUUID("Owner"))
        {
            this.ownerUUID = compound.getUUID("Owner");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", this.age);
        if (this.ownerUUID != null)
        {
            compound.putUUID("Owner", this.ownerUUID);
        }
    }

    // Geckolib
    private final RawAnimation RISE_ANIM = RawAnimation.begin().thenPlay("rise");
    private final RawAnimation FALL_ANIM = RawAnimation.begin().thenPlay("fall");
    private final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idle");

    private final AnimationController<RazorBlade> animationController = new AnimationController<>(this, "controller", 0, this::predicate);
    private final AnimationController<RazorBlade> riseAnimationController = new AnimationController<>(this, "rise_controller", 0, this::risePredicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
        controllerRegistrar.add(riseAnimationController);
    }

    private PlayState predicate(software.bernie.geckolib.animation.AnimationState<RazorBlade> event)
    {
        var controller = event.getController();

        if (age > 100 && Utils.random.nextFloat() < 0.05F)
        {
            controller.setAnimation(FALL_ANIM);
        } else if (controller.getAnimationState() == AnimationController.State.STOPPED)
        {
            controller.setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState<RazorBlade> event)
    {
        var controller = event.getController();

        if (age < 10)
        {
            controller.setAnimation(RISE_ANIM);
            return PlayState.CONTINUE;
        } else
        {
            return PlayState.STOP;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
