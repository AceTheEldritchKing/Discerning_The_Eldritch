package net.acetheeldritchking.discerning_the_eldritch.items.gauntlets;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HandOfApocryphaModel extends AnimatedGeoModel<HandOfApocryphaItem> {
    @Override
    public ResourceLocation getModelResource(HandOfApocryphaItem object) {
        return new ResourceLocation(DiscerningTheEldritch.MOD_ID, "geo/hand_of_apocrypha.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HandOfApocryphaItem object) {
        return new ResourceLocation(DiscerningTheEldritch.MOD_ID, "textures/item/hand_of_apocrypha/hand_of_apocrypha.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HandOfApocryphaItem animatable) {
        return new ResourceLocation(DiscerningTheEldritch.MOD_ID, "animations/item/hand_of_apocrypha.animation.json");
    }
}
