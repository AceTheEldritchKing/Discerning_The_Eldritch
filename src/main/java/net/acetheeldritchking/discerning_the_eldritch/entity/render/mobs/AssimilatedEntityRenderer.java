package net.acetheeldritchking.discerning_the_eldritch.entity.render.mobs;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.acetheeldritchking.discerning_the_eldritch.entity.mobs.sculk_mobs.the_assimilated.AssimilatedModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AssimilatedEntityRenderer extends AbstractSpellCastingMobRenderer {
    public AssimilatedEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AssimilatedModel());
    }
}
