package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.animators.EldritchArmorAnimator;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockArmorRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "geo/eldritch_armor.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
            DiscerningTheEldritch.MOD_ID,
            "textures/models/armor/eldritch_mage_armor.png"
    );

    public EldritchWarlockArmorRenderer() {
        super(
                AzArmorRendererConfig.builder(GEO, TEX)
                        .setAnimatorProvider(EldritchArmorAnimator::new)
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .build()
        );
    }
}
