package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.StarvoidArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class StarvoidArmorModel extends DefaultedItemGeoModel<StarvoidArmorItem> {
    public StarvoidArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(StarvoidArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/starvoid_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StarvoidArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/models/armor/geckolib/starvoid_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StarvoidArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
}
