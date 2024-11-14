package net.acetheeldritchking.discerning_the_eldritch.entity.spells.esoteric_edge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
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

public class EsotericEdgeRenderer extends EntityRenderer<EsotericEdge> {
    private static final ResourceLocation[] TEXTURES = {
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_1.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_2.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_3.png"),
            DiscerningTheEldritch.id("textures/entity/esoteric_edge/esoteric_edge_4.png")
    };

    public EsotericEdgeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EsotericEdge esotericEdge, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        PoseStack.Pose pose = poseStack.last();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, esotericEdge.yRotO, esotericEdge.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTick, esotericEdge.xRotO, esotericEdge.getXRot())));
        float randomZ = new Random(31L * esotericEdge.getId()).nextInt(-8, 8);
        poseStack.mulPose(Axis.XP.rotationDegrees(randomZ));

        createSlashTexturePlace(pose, esotericEdge, bufferSource, esotericEdge.getBbWidth() * 1.5F);

        poseStack.popPose();

        super.render(esotericEdge, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void createSlashTexturePlace(PoseStack.Pose pose, EsotericEdge esotericEdge, MultiBufferSource bufferSource, float width)
    {
        Matrix4f poseMatrix = pose.pose();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(esotericEdge)));

        float halfWidth = width * 0.5F;
        float height = esotericEdge.getBbHeight() * 0.5F;

        consumer.addVertex(poseMatrix, -halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(0F, 1F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(1F, 1F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(1F, 0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
        consumer.addVertex(poseMatrix, -halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(0F, 0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(EsotericEdge esotericEdge) {
        int frame = (esotericEdge.tickCount / 4) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
