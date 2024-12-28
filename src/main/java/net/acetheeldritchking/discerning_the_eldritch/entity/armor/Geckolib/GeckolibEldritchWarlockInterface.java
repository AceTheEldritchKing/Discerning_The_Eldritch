package net.acetheeldritchking.discerning_the_eldritch.entity.armor.Geckolib;

import io.redspace.ironsspellbooks.item.armor.CultistArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockArmorItem;
import net.minecraft.resources.ResourceLocation;

public interface GeckolibEldritchWarlockInterface {
    ResourceLocation getModelResource(GeckolibEldritchWarlockArmorItem object);

    ResourceLocation getTextureResource(GeckolibEldritchWarlockArmorItem object);

    ResourceLocation getAnimationResource(GeckolibEldritchWarlockArmorItem animatable);
}
