package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class PreyPotionEffect extends MobEffect {
    public PreyPotionEffect() {
        super(MobEffectCategory.HARMFUL, 11749198);
    }

    @SubscribeEvent
    public static void summonJawsEvent(LivingDamageEvent.Pre event)
    {
        var entity = event.getEntity();
        var source = event.getSource();
        var sourceEntity = event.getSource().getEntity();

        if (sourceEntity instanceof LivingEntity livingEntity)
        {
            if (livingEntity.hasEffect(DTEPotionEffectRegistry.PREY_POTION_EFFECT))
            {
                // Replace this with our own entity eventually
                DevourJaw jaw = new DevourJaw(livingEntity.level(), livingEntity, entity);

                jaw.setCircular();
                jaw.setPos(entity.position());
                jaw.setYRot(livingEntity.getYRot());
                jaw.setDamage(10);

                livingEntity.level().addFreshEntity(jaw);
            }
        }
    }
}
