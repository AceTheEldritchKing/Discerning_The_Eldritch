package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import net.acetheeldritchking.discerning_the_eldritch.entity.spells.ravenous_jaw.RavenousJawEntity;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class PredatorPotionEffect extends MobEffect {
    protected PredatorPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 11749198);
    }

    @SubscribeEvent
    public static void summonJawsEvent(LivingDamageEvent.Pre event)
    {
        var entity = event.getEntity();
        var source = event.getSource();
        var sourceEntity = event.getSource().getDirectEntity();

        if (sourceEntity instanceof LivingEntity livingEntity)
        {
            // Bonus damage if an entity has the prey effect
            if (livingEntity.hasEffect(DTEPotionEffectRegistry.PREDATOR_POTION_EFFECT))
            {
                RavenousJawEntity jaw = new RavenousJawEntity(livingEntity.level(), livingEntity, entity);

                jaw.setPos(entity.position());
                jaw.setYRot(livingEntity.getYRot());
                jaw.setDamage(10);

                livingEntity.level().addFreshEntity(jaw);
            } else if (livingEntity.hasEffect(DTEPotionEffectRegistry.PREDATOR_POTION_EFFECT) && entity.hasEffect(DTEPotionEffectRegistry.PREY_POTION_EFFECT))
            {
                RavenousJawEntity jaw = new RavenousJawEntity(livingEntity.level(), livingEntity, entity);

                jaw.setPos(entity.position());
                jaw.setYRot(livingEntity.getYRot());
                jaw.setDamage(15);
            }
        }
    }
}
