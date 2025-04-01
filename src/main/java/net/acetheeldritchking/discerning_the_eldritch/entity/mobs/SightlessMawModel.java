package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SightlessMawModel extends DefaultedEntityGeoModel<SightlessMawEntity> {
    public SightlessMawModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "sightless_maw"));
    }

    @Override
    public ResourceLocation getModelResource(SightlessMawEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/sightless_maw.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SightlessMawEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/sightless_maw/sightless_maw.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SightlessMawEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/sightless_maw.animation.json");
    }

    @Override
    public void setCustomAnimations(SightlessMawEntity animatable, long instanceId, AnimationState<SightlessMawEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
