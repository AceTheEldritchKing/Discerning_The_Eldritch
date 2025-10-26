package net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile;

import io.redspace.ironsspellbooks.render.RenderHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class CataclysmBladeSmallModel extends GeoModel<CataclysmBladeSmallProjectile> {
    @Override
    public ResourceLocation getModelResource(CataclysmBladeSmallProjectile cataclysmBladeSmallProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/cataclysm_blade_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CataclysmBladeSmallProjectile cataclysmBladeSmallProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/cataclysm/cataclysm_small.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CataclysmBladeSmallProjectile cataclysmBladeSmallProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/cataclysm_blade_projectile.animation.json");
    }

    /*@Override
    public @Nullable RenderType getRenderType(CataclysmBladeSmallProjectile animatable, ResourceLocation texture) {
        return RenderHelper.CustomerRenderType.magic(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/cataclysm/cataclysm_small.png"));
    }*/
}
