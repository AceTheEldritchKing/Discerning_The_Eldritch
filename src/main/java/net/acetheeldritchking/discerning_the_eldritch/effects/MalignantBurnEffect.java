package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class MalignantBurnEffect extends MobEffect {
    public MalignantBurnEffect() {
        super(MobEffectCategory.HARMFUL, 15412568);
        this.addAttributeModifier(AttributeRegistry.SPELL_RESIST, DiscerningTheEldritch.id("malignant_burn_potion_effect"), -0.55F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
