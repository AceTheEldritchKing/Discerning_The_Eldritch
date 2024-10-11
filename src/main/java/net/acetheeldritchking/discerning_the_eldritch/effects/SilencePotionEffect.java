package net.acetheeldritchking.discerning_the_eldritch.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class SilencePotionEffect extends MobEffect {
    public SilencePotionEffect() {
        super(MobEffectCategory.HARMFUL, 3755355);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Actually, I DO know what I'm doing!
    }
}
