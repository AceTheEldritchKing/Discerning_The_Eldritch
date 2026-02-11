package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.particle.DTEParticleHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
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
public class RuinPotionEffect extends MobEffect {
    public RuinPotionEffect() {
        super(MobEffectCategory.HARMFUL, 4534333);
    }

    @SubscribeEvent
    public static void onHurtEvent(LivingIncomingDamageEvent event)
    {
        Entity attacker = event.getSource().getEntity();
        Entity victim = event.getEntity();
        DamageSource source = event.getSource();

        // Increase Eldritch damage
        if (source.is(ISSDamageTypes.ELDRITCH_MAGIC))
        {
            event.setAmount(event.getAmount() * 1.2F);
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide())
        {
            ServerLevel world = (ServerLevel) livingEntity.level();

            world.sendParticles(ParticleHelper.SIPHON, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
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
