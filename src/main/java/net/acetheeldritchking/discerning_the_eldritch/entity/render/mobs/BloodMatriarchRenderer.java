package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.bosses.minibosses.blood_matriarch.BloodMatriarchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodMatriarchRenderer extends AbstractSpellCastingMobRenderer {
    public BloodMatriarchRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodMatriarchModel());
    }
}
