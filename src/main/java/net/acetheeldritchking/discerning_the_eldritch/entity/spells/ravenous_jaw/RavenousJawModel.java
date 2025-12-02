package net.acetheeldritchking.discerning_the_eldritch.entity.spells.ravenous_jaw;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RavenousJawModel extends GeoModel<RavenousJawEntity> {
    @Override
    public ResourceLocation getModelResource(RavenousJawEntity ravenousJawEntity) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/ravenous_jaw.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RavenousJawEntity ravenousJawEntity) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/ravenous_jaw/ravenous_jaw.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RavenousJawEntity ravenousJawEntity) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/ravenous_jaw.animation.json");
    }
}
