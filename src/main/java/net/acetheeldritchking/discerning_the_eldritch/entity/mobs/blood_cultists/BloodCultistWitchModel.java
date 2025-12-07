package net.acetheeldritchking.discerning_the_eldritch.entity.mobs.blood_cultists;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;

public class BloodCultistWitchModel extends AbstractSpellCastingMobModel {
    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/blood_cultists/blood_witch.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob object) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "animations/casting_animations.json");
    }
}
