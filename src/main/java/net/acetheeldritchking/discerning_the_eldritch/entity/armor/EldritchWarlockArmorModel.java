package net.acetheeldritchking.discerning_the_eldritch.entity.armor;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.old.OldEldritchWarlockArmorItem;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockArmorModel extends GeoModel<OldEldritchWarlockArmorItem> {
    @Override
    public ResourceLocation getModelResource(OldEldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OldEldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_mage_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OldEldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor.animation.json");
    }
}
