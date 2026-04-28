package net.acetheeldritchking.discerning_the_eldritch.entity.spells.voidsplitter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VoidsplitterRenderer extends GeoEntityRenderer<VoidsplitterProjectile> {
    public VoidsplitterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VoidsplitterModel());
    }

    @Override
    public ResourceLocation getTextureLocation(VoidsplitterProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/voidsplitter/voidsplitter.png");
    }

    @Override
    public boolean shouldRender(VoidsplitterProjectile livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }

    @Override
    public void preRender(PoseStack poseStack, VoidsplitterProjectile entity, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTicks, int packedLight, int packedOverlay, int colour) {
        Vec3 motion = animatable.getLookAngle().scale(-1);
        float xRot = - ((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90);
        float yRot = - ((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTicks, packedLight, packedOverlay, colour);
    }
}
