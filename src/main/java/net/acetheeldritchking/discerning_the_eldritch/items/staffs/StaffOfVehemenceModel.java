package net.acetheeldritchking.discerning_the_eldritch.items.staffs;

import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;

public class StaffOfVehemenceModel extends GeoModel<StaffOfVehemenceStaffItem> {
    @Override
    public ResourceLocation getModelResource(StaffOfVehemenceStaffItem staffOfVehemenceStaffItem) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/staff_of_eldritch.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StaffOfVehemenceStaffItem staffOfVehemenceStaffItem) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/item/staff_of_eldritch/staff_of_eldritch.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StaffOfVehemenceStaffItem staffOfVehemenceStaffItem) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/staff_of_eldritch.animation.json");
    }
}
