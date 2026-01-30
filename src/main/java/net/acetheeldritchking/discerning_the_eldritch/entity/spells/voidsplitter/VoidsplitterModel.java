package net.acetheeldritchking.discerning_the_eldritch.entity.spells.voidsplitter;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidsplitterModel extends GeoModel<VoidsplitterProjectile> {
    @Override
    public ResourceLocation getModelResource(VoidsplitterProjectile voidsplitterProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/voidsplitter_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidsplitterProjectile voidsplitterProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/voidsplitter/voidsplitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidsplitterProjectile voidsplitterProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/voidsplitter_projectile.animation.json");
    }
}
