package net.acetheeldritchking.discerning_the_eldritch.entity.spells.cataclysm_blade_projectile;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CataclysmBladeBigModel extends GeoModel<CataclysmBladeBigProjectile> {
    @Override
    public ResourceLocation getModelResource(CataclysmBladeBigProjectile cataclysmBladeBigProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/cataclysm_blade_big_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CataclysmBladeBigProjectile cataclysmBladeBigProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/cataclysm/cataclysm_big.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CataclysmBladeBigProjectile cataclysmBladeBigProjectile) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/cataclysm_blade_projectile.animation.json");
    }
}
