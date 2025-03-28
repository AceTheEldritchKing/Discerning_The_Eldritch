package net.acetheeldritchking.discerning_the_eldritch.items.staffs;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import mod.azure.azurelib.common.api.common.animatable.GeoItem;
import mod.azure.azurelib.common.internal.client.RenderProvider;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.acetheeldritchking.discerning_the_eldritch.entity.render.items.StaffOfVehemenceRenderer;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Rarity;

import java.util.function.Consumer;

public class StaffOfVehemenceStaffItem extends ImbuableStaffItem implements GeoItem {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public StaffOfVehemenceStaffItem() {
        super(
                ItemPropertiesHelper.equipment().fireResistant().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(DTEStaffTier.STAFF_OF_VEHEMENCE)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.ESOTERIC_EDGE, 6),
                        new SpellDataRegistryHolder(SpellRegistries.SILENCE, 6))
        );
    }

    @Override
    public void createRenderer(Consumer<RenderProvider> consumer) {
        consumer.accept(new RenderProvider() {
            private StaffOfVehemenceRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                this.renderer = new StaffOfVehemenceRenderer();
                return this.renderer;
            }
        });
    }

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");

    private final AnimationController<StaffOfVehemenceStaffItem> animationController = new AnimationController<>(this, "controller", 0, this::predicate);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
    }

    private PlayState predicate(AnimationState<StaffOfVehemenceStaffItem> event)
    {
        event.getController().setAnimation(IDLE_ANIMATION);

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
