package net.acetheeldritchking.discerning_the_eldritch.entity.armor;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockArmorItem;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockArmorModel extends GeoModel<EldritchWarlockArmorItem> {
    @Override
    public ResourceLocation getModelResource(EldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_mage_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor.animation.json");
    }
}
