package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class StardustedPotionEffect extends MobEffect {
    public StardustedPotionEffect() {
        super(MobEffectCategory.NEUTRAL, 16763791);
    }

    @SubscribeEvent
    public static void onHurtEvent(LivingIncomingDamageEvent event)
    {
        Entity attacker = event.getSource().getEntity();
        Entity victim = event.getEntity();
        DamageSource source = event.getSource();

        if (victim instanceof LivingEntity livingTarget && livingTarget.hasEffect(DTEPotionEffectRegistry.STARDUSTED_EFFECT)) {
            //DiscerningTheEldritch.LOGGER.debug("Poof!");
            livingTarget.removeEffect(DTEPotionEffectRegistry.STARDUSTED_EFFECT);
        }

        // Increase Ender damage
        if (source.is(ISSDamageTypes.ENDER_MAGIC))
        {
            event.setAmount(event.getAmount() * 1.2F);
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide())
        {
            ServerLevel world = (ServerLevel) livingEntity.level();

            world.sendParticles(DTEParticleHelper.STARDUST, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
                    15,
                    world.random.nextGaussian() * 0.017D,
                    world.random.nextGaussian() * 0.017D,
                    world.random.nextGaussian() * 0.017D,
                    0.02D);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
