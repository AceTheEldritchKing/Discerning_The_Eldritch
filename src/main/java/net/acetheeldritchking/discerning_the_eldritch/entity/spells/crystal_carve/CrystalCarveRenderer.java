package net.acetheeldritchking.discerning_the_eldritch.entity.spells.crystal_carve;

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
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Random;

public class CrystalCarveRenderer extends EntityRenderer<CrystalCarveEntity> {
    private static final ResourceLocation[] TEXTURES = {
            DiscerningTheEldritch.id("textures/entity/crystal_carve/crystal_carve_1.png"),
            DiscerningTheEldritch.id("textures/entity/crystal_carve/crystal_carve_2.png"),
            DiscerningTheEldritch.id("textures/entity/crystal_carve/crystal_carve_3.png"),
            DiscerningTheEldritch.id("textures/entity/crystal_carve/crystal_carve_4.png")
    };
    
    public CrystalCarveRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CrystalCarveEntity carver, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        PoseStack.Pose pose = poseStack.last();
        poseStack.mulPose(Axis.YP.rotationDegrees(90 - carver.getYRot()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(carver.getXRot()));
        float randomZ = new Random(31L * carver.getId()).nextInt(-5, 5);
        poseStack.mulPose(Axis.XP.rotationDegrees(randomZ));

        drawSlash(pose, carver, bufferSource, carver.getBbWidth() * 1.5F, carver.isMirrored());

        poseStack.popPose();

        super.render(carver, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void drawSlash(PoseStack.Pose pose, CrystalCarveEntity entity, MultiBufferSource bufferSource, float width, boolean mirrored)
    {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        float halfWidth = width * 0.5F;
        float height = entity.getBbHeight() * 0.5F;

        consumer.addVertex(poseMatrix, -halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(0f, mirrored ? 0f : 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0f, 1f, 0f);
        consumer.addVertex(poseMatrix, halfWidth, height, -halfWidth).setColor(255, 255, 255, 255).setUv(1f, mirrored ? 0f : 1f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0f, 1f, 0f);
        consumer.addVertex(poseMatrix, halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(1f, mirrored ? 1f : 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0f, 1f, 0f);
        consumer.addVertex(poseMatrix, -halfWidth, height, halfWidth).setColor(255, 255, 255, 255).setUv(0f, mirrored ? 1f : 0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0f, 1f, 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalCarveEntity crystalCarveEntity) {
        int frame = (crystalCarveEntity.tickCount / crystalCarveEntity.ticksPerFrame) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
