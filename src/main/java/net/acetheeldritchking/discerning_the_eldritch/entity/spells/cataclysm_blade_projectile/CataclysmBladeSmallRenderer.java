package net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class CataclysmBladeSmallRenderer extends GeoEntityRenderer<CataclysmBladeSmallProjectile> {
    public CataclysmBladeSmallRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CataclysmBladeSmallModel());
    }

    @Override
    public ResourceLocation getTextureLocation(CataclysmBladeSmallProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/cataclysm/cataclysm_small.png");
    }

    @Override
    public void preRender(PoseStack poseStack, CataclysmBladeSmallProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTicks, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTicks, packedLight, packedOverlay, colour);
        Vec3 motion = animatable.deltaMovementOld.add(animatable.getDeltaMovement().subtract(animatable.deltaMovementOld).scale(partialTicks));
        float xRot = ((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) - 90.0F);
        poseStack.translate(0, animatable.getBbHeight() * .5f, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
    }

    @Override
    public @Nullable RenderType getRenderType(CataclysmBladeSmallProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.breezeEyes(texture);
    }
}
