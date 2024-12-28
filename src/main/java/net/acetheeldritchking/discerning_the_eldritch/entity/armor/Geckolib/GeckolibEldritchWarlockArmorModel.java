package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class GeckolibEldritchWarlockArmorModel extends DefaultedItemGeoModel<GeckolibEldritchWarlockArmorItem> implements GeckolibEldritchWarlockInterface {

    public GeckolibEldritchWarlockArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(GeckolibEldritchWarlockArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/eldritch_armor_geckolib.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckolibEldritchWarlockArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/eldritch_mage_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeckolibEldritchWarlockArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/eldritch_armor_geckolib.animation.json");
    }
}
