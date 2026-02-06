package net.acetheeldritchking.discerning_the_eldritch.entity.render.items.lantern_of_hatred;

import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.common.render.layer.AzAutoGlowingLayer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.animators.LanternOfHatredEquippedAnimator;
import net.minecraft.resources.ResourceLocation;

public class LanternOfHatredEquippedItemRenderer extends AzArmorRenderer {
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "geo/lantern_of_hatred_equipped.geo.json"
    );

    public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "textures/models/armor/lantern_of_hatred_model.png"
    );

    public LanternOfHatredEquippedItemRenderer() {
        super(
                AzArmorRendererConfig.builder(GEO, TEX)
                        .setAnimatorProvider(LanternOfHatredEquippedAnimator::new)
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .build()
        );
    }
}
