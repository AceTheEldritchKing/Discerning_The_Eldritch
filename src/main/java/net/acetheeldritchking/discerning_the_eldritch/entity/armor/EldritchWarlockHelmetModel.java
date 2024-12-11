package net.acetheeldritchking.discerning_the_eldritch.entity.armor;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockHelmetItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockMaskItem;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockHelmetModel extends GeoModel<EldritchWarlockHelmetItem> {
    @Override
    public ResourceLocation getModelResource(EldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor_helmet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_armor_helmet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EldritchWarlockHelmetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor.animation.json");
    }
}
