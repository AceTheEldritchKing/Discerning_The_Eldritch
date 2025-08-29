package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.eldritch_caster.TheApostleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class TheApostleRenderer extends HumanoidRenderer<TheApostleEntity> {
    public TheApostleRenderer(EntityRendererProvider.Context renderManager, GeoModel<TheApostleEntity> model) {
        super(renderManager, model);
        this.shadowRadius = 1.5f;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TheApostleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/apostle/apostle.png");
    }

    @Override
    public void render(TheApostleEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
