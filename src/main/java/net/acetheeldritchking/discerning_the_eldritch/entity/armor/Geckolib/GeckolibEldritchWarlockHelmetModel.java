package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockHelmetItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class GeckolibEldritchWarlockHelmetModel extends DefaultedItemGeoModel<GeckolibEldritchWarlockHelmetItem> implements GeckolibEldritchHelmetInterface {

    public GeckolibEldritchWarlockHelmetModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(GeckolibEldritchWarlockHelmetModel object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor_helmet_geckolib.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckolibEldritchWarlockHelmetModel object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_armor_helmet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeckolibEldritchWarlockHelmetModel animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor_geckolib.animation.json");
    }
}
