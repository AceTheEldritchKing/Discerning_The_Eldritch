package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sculk_mobs.the_assimilated;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;

public class AssimilatedModel extends AbstractSpellCastingMobModel {
    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/assimilated/assimilated.png");
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "geo/assimilated_entity.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/casting_animations.json");
    }
}
