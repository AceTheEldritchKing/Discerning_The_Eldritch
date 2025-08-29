package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.eldritch_caster;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TheApostleModel extends DefaultedEntityGeoModel<TheApostleEntity> {
    public TheApostleModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apostle"));
    }

    @Override
    public ResourceLocation getModelResource(TheApostleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/apostle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TheApostleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/apostle/apostle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TheApostleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/apostle.animation.json");
    }

    @Override
    public void setCustomAnimations(TheApostleEntity animatable, long instanceId, AnimationState<TheApostleEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
