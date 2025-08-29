package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.untold_behemoth.UntoldBehemothEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UntoldBehemothRenderer extends GeoEntityRenderer<UntoldBehemothEntity> {
    public UntoldBehemothRenderer(EntityRendererProvider.Context renderManager, GeoModel<UntoldBehemothEntity> model) {
        super(renderManager, model);
        this.shadowRadius = 1f;
    }

    @Override
    public ResourceLocation getTextureLocation(UntoldBehemothEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/untold_behemoth/untold_behemoth.png");
    }

    @Override
    public void render(UntoldBehemothEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
