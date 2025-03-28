package net.acetheeldritchking.discerning_the_eldritch.entity.render.items;

import mod.azure.azurelib.common.api.client.renderer.GeoItemRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.AutoGlowingGeoLayer;
import net.acetheeldritchking.discerning_the_eldritch.items.staffs.StaffOfVehemenceModel;
import net.acetheeldritchking.discerning_the_eldritch.items.staffs.StaffOfVehemenceStaffItem;

public class StaffOfVehemenceRenderer extends GeoItemRenderer<StaffOfVehemenceStaffItem> {
    public StaffOfVehemenceRenderer() {
        super(new StaffOfVehemenceModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
