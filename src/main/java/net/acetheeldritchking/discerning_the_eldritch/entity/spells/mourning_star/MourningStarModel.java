package net.acetheeldritchking.discerning_the_eldritch.entity.spells.mourning_star;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MourningStarModel extends GeoModel<MourningStarProjectile> {
    @Override
    public ResourceLocation getModelResource(MourningStarProjectile mourningStarProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/mourning_star_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MourningStarProjectile mourningStarProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/mourning_star/mourning_star_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MourningStarProjectile mourningStarProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/mourning_star_projectile.animation.json");
    }
}
