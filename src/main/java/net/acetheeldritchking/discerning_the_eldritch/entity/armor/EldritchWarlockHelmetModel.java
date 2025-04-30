package net.acetheeldritchking.discerning_the_eldritch.entity.armor;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.old.OldEldritchWarlockHelmetItem;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockHelmetModel extends GeoModel<OldEldritchWarlockHelmetItem> {
    @Override
    public ResourceLocation getModelResource(OldEldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor_helmet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OldEldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_armor_helmet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OldEldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor.animation.json");
    }
}
