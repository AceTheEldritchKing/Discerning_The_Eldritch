package net.acetheeldritchking.discerning_the_eldritch.items.armor.animators;

import mod.azure.azurelib.rewrite.animation.AzAnimator;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EldritchArmorAnimator extends AzAnimator {
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "animations/eldritch_armor.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, "base_controller")
                        .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Object animatable) {
        return ANIMATIONS;
    }
}
