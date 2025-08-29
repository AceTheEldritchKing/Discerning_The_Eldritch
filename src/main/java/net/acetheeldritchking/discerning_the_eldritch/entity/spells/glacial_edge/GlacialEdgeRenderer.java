package net.acetheeldritchking.discerning_the_eldritch.entity.spells.glacial_edge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge.EsotericEdge;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.Random;

public class GlacialEdgeRenderer extends EntityRenderer<GlacialEdge> {
    private static final ResourceLocation TEX = DiscerningTheEldritch.id("textures/entity/glacial_edge/glacial_edge.png");

    public GlacialEdgeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GlacialEdge glacialEdge, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        PoseStack.Pose pose = poseStack.last();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, glacialEdge.yRotO, glacialEdge.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTick, glacialEdge.xRotO, glacialEdge.getXRot())));
        float randomZ = new Random(31L * glacialEdge.getId()).nextInt(-8, 8);
        poseStack.mulPose(Axis.XP.rotationDegrees(randomZ));

        createSlashTexturePlace(pose, glacialEdge, bufferSource, glacialEdge.getBbWidth() * 1.5F);

        poseStack.popPose();

        super.render(glacialEdge, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void createSlashTexturePlace(PoseStack.Pose pose, GlacialEdge glacialEdge, MultiBufferSource bufferSource, float width)
    {
        Matrix4f poseMatrix = pose.pose();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(glacialEdge)));

        float halfWidth = width * 0.5F;
        float height = glacialEdge.getBbHeight() * 0.5F;

        consumer.addVertex(poseMatrix, -halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(0F, 1F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(1F, 1F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(1F, 0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, -halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(0F, 0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(GlacialEdge glacialEdge) {
        return TEX;
    }
}
