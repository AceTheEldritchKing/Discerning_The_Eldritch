package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockMaskItem;
import net.minecraft.resources.ResourceLocation;

public interface GeckolibEldritchMaskInterface {
    ResourceLocation getModelResource(GeckolibEldritchWarlockMaskItem object);

    ResourceLocation getTextureResource(GeckolibEldritchWarlockMaskItem object);

    ResourceLocation getAnimationResource(GeckolibEldritchWarlockMaskItem animatable);
}
