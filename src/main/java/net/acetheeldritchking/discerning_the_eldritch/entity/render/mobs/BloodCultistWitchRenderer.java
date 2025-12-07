package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.blood_cultists.BloodCultistWitchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodCultistWitchRenderer extends AbstractSpellCastingMobRenderer {
    public BloodCultistWitchRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodCultistWitchModel());
    }
}
