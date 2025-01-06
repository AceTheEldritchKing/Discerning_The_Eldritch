package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GaolerModel extends GeoModel<GaolerEntity> {
    @Override
    public ResourceLocation getModelResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/gaoler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/gaoler/gaoler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/gaoler.animation.json");
    }
}
