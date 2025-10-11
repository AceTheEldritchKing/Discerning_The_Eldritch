package net.acetheeldritchking.discerning_the_eldritch.entity.render.armor;

import mod.azure.azurelib.common.internal.client.util.RenderUtils;
import mod.azure.azurelib.rewrite.model.AzBakedModel;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.AzRendererPipeline;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererPipeline;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.armor.bone.AzArmorBoneContext;
import mod.azure.azurelib.rewrite.render.armor.bone.AzArmorBoneProvider;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.animators.EldritchArmorAnimator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

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
                        .setPipelineContext(AzArmorLowerCloakLayerPipeline::new)
                        .build()
        );
    }
}
