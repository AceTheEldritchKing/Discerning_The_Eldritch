package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicTraitorModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ApothicTraitorRenderer extends AbstractSpellCastingMobRenderer {
    public ApothicTraitorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ApothicTraitorModel());
    }
}
