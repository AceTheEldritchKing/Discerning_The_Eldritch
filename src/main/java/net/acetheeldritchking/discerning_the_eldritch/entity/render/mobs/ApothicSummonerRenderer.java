package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicSummonerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ApothicSummonerRenderer extends AbstractSpellCastingMobRenderer {
    public ApothicSummonerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ApothicSummonerModel());
    }
}
