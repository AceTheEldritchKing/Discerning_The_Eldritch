package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BloodRotPotionEffect extends MobEffect {
    public static DamageSource DAMAGE_SOURCE;

    public BloodRotPotionEffect() {
        super(MobEffectCategory.HARMFUL, 11749198);
        this.addAttributeModifier(AttributeRegistry.BLOOD_SPELL_POWER, DiscerningTheEldritch.id("blood_rot_potion_effect"), -0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        this.addAttributeModifier(AttributeRegistry.BLOOD_MAGIC_RESIST, DiscerningTheEldritch.id("blood_rot_potion_effect"), -0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide)
        {
            ServerLevel world = (ServerLevel) livingEntity.level();

            if (DAMAGE_SOURCE == null)
            {
                DAMAGE_SOURCE = new DamageSource(DamageSources.getHolderFromResource(livingEntity, DTEDamageTypes.BLOOD_ROT));
            }
            DamageSources.ignoreNextKnockback(livingEntity);
            livingEntity.hurt(DAMAGE_SOURCE, 3);

            world.sendParticles(ParticleHelper.BLOOD, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
                    15,
                    world.random.nextGaussian() * 0.007D,
                    world.random.nextGaussian() * 0.007D,
                    world.random.nextGaussian() * 0.007D,
                    0.05D);
            world.sendParticles(ParticleHelper.BLOOD_GROUND, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
                    5,
                    world.random.nextGaussian() * 0.007D,
                    world.random.nextGaussian() * 0.007D,
                    world.random.nextGaussian() * 0.007D,
                    0.05D);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
