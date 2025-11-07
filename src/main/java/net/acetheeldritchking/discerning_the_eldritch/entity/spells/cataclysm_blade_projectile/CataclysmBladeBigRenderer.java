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
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CataclysmBladeBigRenderer extends GeoEntityRenderer<CataclysmBladeBigProjectile> {
    public CataclysmBladeBigRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CataclysmBladeBigModel());
    }

    @Override
    public ResourceLocation getTextureLocation(CataclysmBladeBigProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/cataclysm/cataclysm_big.png");
    }

    @Override
    public void preRender(PoseStack poseStack, CataclysmBladeBigProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTicks, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTicks, packedLight, packedOverlay, colour);
        Vec3 motion = animatable.deltaMovementOld.add(animatable.getDeltaMovement().subtract(animatable.deltaMovementOld).scale(partialTicks));
        float xRot = ((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) - 90.0F);
        poseStack.translate(0, animatable.getBbHeight() * .5f, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
    }

    @Override
    public @Nullable RenderType getRenderType(CataclysmBladeBigProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.breezeEyes(texture);
    }
}
