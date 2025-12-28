package net.acetheeldritchking.discerning_the_eldritch.entity.spells.mourning_star;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MourningStarRenderer extends GeoEntityRenderer<MourningStarProjectile> {
    public MourningStarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MourningStarModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MourningStarProjectile animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/mourning_star/mourning_star_projectile.png");
    }

    @Override
    public @Nullable RenderType getRenderType(MourningStarProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.breezeEyes(texture);
    }

    @Override
    public boolean shouldRender(MourningStarProjectile livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }
}
