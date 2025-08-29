package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.untold_behemoth;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class UntoldBehemothModel extends DefaultedEntityGeoModel<UntoldBehemothEntity> {
    public UntoldBehemothModel() {
        super(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "untold_behemoth"));
    }

    @Override
    public ResourceLocation getModelResource(UntoldBehemothEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/untold_behemoth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UntoldBehemothEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/untold_behemoth/untold_behemoth.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UntoldBehemothEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/untold_behemoth.animation.json");
    }

    @Override
    public void setCustomAnimations(UntoldBehemothEntity animatable, long instanceId, AnimationState<UntoldBehemothEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
