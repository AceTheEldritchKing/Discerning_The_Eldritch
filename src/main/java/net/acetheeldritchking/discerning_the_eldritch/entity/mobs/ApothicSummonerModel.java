package net.acetheeldritchking.discerning_the_eldritch.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;

public class ApothicSummonerModel extends AbstractSpellCastingMobModel {
    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        return ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "textures/entity/apothic_cultists/apothic_summoner.png");
    }
}
