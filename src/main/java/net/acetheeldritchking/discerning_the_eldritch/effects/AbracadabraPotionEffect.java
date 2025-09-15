package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber
public class AbracadabraPotionEffect extends CustomDescriptionMobEffect {
    public AbracadabraPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 15661670);
    }

    public static final float BASE_DAMAGE_CAP = 100.0F;

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        float damageCapAmount = getDamageCapAmount(amp, 0);
        return Component.translatable("tooltip.discerning_the_eldritch.abracadabra_description", (int) (damageCapAmount)).withStyle(ChatFormatting.BLUE);
    }

    public static float getDamageCapAmount(int level, float damage)
    {
        return Mth.clamp(damage, 0, (BASE_DAMAGE_CAP / level));
    }

    @SubscribeEvent
    public static void damageCapEvent(LivingIncomingDamageEvent event)
    {
        var entity = event.getEntity();
        var effect = entity.getEffect(DTEPotionEffectRegistry.ABRACADABRA_EFFECT);

        if (effect != null)
        {
            int level = effect.getAmplifier() + 1;
            float baseDamage = event.getOriginalAmount();
            float newDamage = getDamageCapAmount(level, baseDamage);

            // Just making sure this doesn't go into the negatives
            event.setAmount(newDamage);
            DiscerningTheEldritch.LOGGER.debug("Old damage: " + baseDamage);
            DiscerningTheEldritch.LOGGER.debug("Damage cap: " + newDamage);
            DiscerningTheEldritch.LOGGER.debug("New damage: " + event.getAmount());
        }
    }

    @SubscribeEvent
    public static void preventHexes(MobEffectEvent.Applicable event)
    {
        LivingEntity entity = event.getEntity();
        var hexEffect = event.getEffectInstance().getEffect();
        var effect = entity.getEffect(DTEPotionEffectRegistry.ABRACADABRA_EFFECT);

        if (effect != null && !hexEffect.is(DTETags.BYPASS_ABRACADABRA) && hexEffect.value().getCategory() == MobEffectCategory.HARMFUL)
        {
            DiscerningTheEldritch.LOGGER.debug("Prevented a hex");
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
