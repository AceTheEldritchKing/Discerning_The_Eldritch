package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASUtils;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class ScorchedSoulPotionEffect extends MobEffect {
    public ScorchedSoulPotionEffect() {
        super(MobEffectCategory.HARMFUL, 2598645);
    }

    @SubscribeEvent
    public static void livingDamageEventPre(LivingIncomingDamageEvent event)
    {
        var entity = event.getEntity();
        var source = event.getSource();
        var attacker = event.getSource().getEntity();

        // Take extra damage if it's ISS fire damage or if the entity is on fire
        // Attacker has to be holding the Soul Fire Scythe
        if (attacker != null && attacker.getWeaponItem() != null && attacker.getWeaponItem().is(ItemRegistries.SOUL_FIRE_SCYTHE.get()))
        {
            if (!ASUtils.isBossEntity(entity.getType()) && ((source.is(ISSDamageTypes.FIRE_MAGIC) || entity.isOnFire()) && entity.hasEffect(DTEPotionEffectRegistry.SCORCHED_SOUL_EFFECT)))
            {
                float baseDamage = event.getAmount();
                float newDamage = baseDamage * 1.5F;
                event.setAmount(newDamage);
                //DiscerningTheEldritch.LOGGER.debug("New damage: " + newDamage);
                //DiscerningTheEldritch.LOGGER.debug("Base damage: " + baseDamage);
                //DiscerningTheEldritch.LOGGER.debug("Total damage: " + event.getNewDamage());
            }
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide())
        {
            ServerLevel world = (ServerLevel) livingEntity.level();

            world.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(),
                    15,
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
