package net.acetheeldritchking.discerning_the_eldritch.effects;

import net.acetheeldritchking.discerning_the_eldritch.registries.DTESoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AuditoryHallucinationPotionEffect extends MobEffect {
    public AuditoryHallucinationPotionEffect() {
        super(MobEffectCategory.HARMFUL, 1710620);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide)
        {
            livingEntity.level().playLocalSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), DTESoundRegistry.INSANITY_VOICES.get(), SoundSource.AMBIENT, 1, 1, false);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }
}
