package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.GaolerEntity;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.GaolerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class GaolerRenderer extends GeoEntityRenderer<GaolerEntity> {
    public GaolerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GaolerModel());
        this.shadowRadius = 1.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/gaoler/gaoler.png");
    }

    @Override
    public void render(GaolerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
