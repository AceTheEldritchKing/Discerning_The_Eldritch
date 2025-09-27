package net.acetheeldritchking.discerning_the_eldritch.entity.spells.razor_blade;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RazorBladeModel extends GeoModel<RazorBlade> {
    @Override
    public ResourceLocation getModelResource(RazorBlade razorBlade) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/razor_blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RazorBlade razorBlade) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/razor_blade/razor_blade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RazorBlade razorBlade) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/razor_blade.animation.json");
    }
}
