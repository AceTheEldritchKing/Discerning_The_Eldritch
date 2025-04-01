package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.SightlessMawEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SightlessMawRenderer extends GeoEntityRenderer<SightlessMawEntity> {
    public SightlessMawRenderer(EntityRendererProvider.Context renderManager, GeoModel<SightlessMawEntity> model) {
        super(renderManager, model);
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(SightlessMawEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/sightless_maw/sightless_maw.png");
    }

    @Override
    public void render(SightlessMawEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
