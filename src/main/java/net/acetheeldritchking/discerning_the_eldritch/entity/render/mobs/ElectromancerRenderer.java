package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.electromancer.ElectromancerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ElectromancerRenderer extends AbstractSpellCastingMobRenderer {
    public ElectromancerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ElectromancerModel());
    }
}
