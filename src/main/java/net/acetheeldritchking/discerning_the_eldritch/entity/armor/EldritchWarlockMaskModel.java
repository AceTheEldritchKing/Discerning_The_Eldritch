package net.acetheeldritchking.discerning_the_eldritch.entity.armor;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockMaskItem;
import net.minecraft.resources.ResourceLocation;

public class EldritchWarlockMaskModel extends GeoModel<EldritchWarlockMaskItem> {
    @Override
    public ResourceLocation getModelResource(EldritchWarlockMaskItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor_mask.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EldritchWarlockMaskItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_warlock_mask.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EldritchWarlockMaskItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor.animation.json");
    }
}
