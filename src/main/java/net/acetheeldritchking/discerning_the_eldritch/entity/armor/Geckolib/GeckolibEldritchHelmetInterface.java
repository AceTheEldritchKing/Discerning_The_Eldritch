package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockHelmetItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockMaskItem;
import net.minecraft.resources.ResourceLocation;

public interface GeckolibEldritchHelmetInterface {
    ResourceLocation getModelResource(GeckolibEldritchWarlockHelmetItem object);

    ResourceLocation getTextureResource(GeckolibEldritchWarlockHelmetItem object);

    ResourceLocation getAnimationResource(GeckolibEldritchWarlockHelmetItem animatable);
}
