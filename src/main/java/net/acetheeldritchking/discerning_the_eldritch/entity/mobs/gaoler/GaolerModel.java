package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.gaoler;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GaolerModel extends DefaultedEntityGeoModel<GaolerEntity> {
    public GaolerModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "gaoler"));
    }

    @Override
    public ResourceLocation getModelResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/gaoler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/gaoler/gaoler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GaolerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/gaoler.animation.json");
    }

    @Override
    public void setCustomAnimations(GaolerEntity animatable, long instanceId, AnimationState<GaolerEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
