package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.apothic_cultists.ApothicAcolyteModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ApothicAcolyteRenderer extends AbstractSpellCastingMobRenderer {
    public ApothicAcolyteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ApothicAcolyteModel());
    }
}
