package net.acetheeldritchking.discerning_the_eldritch.items.gauntlets;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.items.item_render.HandOfApocryphaRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class HandOfApocryphaItem extends CastingGauntletItem implements IAnimatable, ISyncable {
    private static final int ACTIVE_ANIM_STATE = 0;
    private static final AnimationBuilder ACTIVE_ANIM = new AnimationBuilder().addAnimation("casting", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public HandOfApocryphaItem() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC), 9, -3.5,
                Map.of(
                        AttributeRegistry.ELDRITCH_SPELL_POWER.get(),
                        new AttributeModifier(UUID.fromString("307c7972-eab7-45ec-8a5b-2462e46920c5"), "Weapon modifier", 0.20F, AttributeModifier.Operation.MULTIPLY_BASE),
                        AttributeRegistry.SPELL_POWER.get(),
                        new AttributeModifier(UUID.fromString("307c7972-eab7-45ec-8a5b-2462e46920c5"), "Weapon modifier", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE),
                        AttributeRegistry.COOLDOWN_REDUCTION.get(),
                        new AttributeModifier(UUID.fromString("307c7972-eab7-45ec-8a5b-2462e46920c5"), "Weapon modifier", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE)
                ));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "Activation", 20, event -> PlayState.CONTINUE));
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == ACTIVE_ANIM_STATE)
        {
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, "Activation");

            if (controller.getAnimationState() == AnimationState.Stopped)
            {
                controller.markNeedsReload();
                controller.setAnimation(ACTIVE_ANIM);
            }
        }
    }

    private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event)
    {
        AnimationBuilder builder = new AnimationBuilder();
        builder.addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
        event.getController().setAnimation(builder);

        return PlayState.CONTINUE;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new HandOfApocryphaRenderer();

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
