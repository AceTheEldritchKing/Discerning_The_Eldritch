package net.acetheeldritchking.discerning_the_eldritch.items.item_render;

import net.acetheeldritchking.discerning_the_eldritch.items.gauntlets.HandOfApocryphaItem;
import net.acetheeldritchking.discerning_the_eldritch.items.gauntlets.HandOfApocryphaModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HandOfApocryphaRenderer extends GeoItemRenderer<HandOfApocryphaItem> {
    public HandOfApocryphaRenderer() {
        super(new HandOfApocryphaModel());
    }
}
