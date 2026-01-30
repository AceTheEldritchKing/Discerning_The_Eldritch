package net.acetheeldritchking.discerning_the_eldritch.entity.spells.voidsplitter;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
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
    public @Nullable RenderType getRenderType(VoidsplitterProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.breezeEyes(texture);
    }

    @Override
    public boolean shouldRender(VoidsplitterProjectile livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }
}
