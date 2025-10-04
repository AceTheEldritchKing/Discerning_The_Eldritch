package net.acetheeldritchking.discerning_the_eldritch.effects;

import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.util.ModTags;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEPotionEffectRegistry;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTEConfig;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTETags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
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

    //public static final int BASE_DAMAGE_CAP = DTEConfig.abracadabraDamageCap;

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        float damageCapAmount = getCapAmount(amp);
        return Component.translatable("tooltip.discerning_the_eldritch.abracadabra_description", (int) (damageCapAmount)).withStyle(ChatFormatting.BLUE);
    }

    public static float getCapAmount(int level)
    {
        return (float) DTEConfig.abracadabraDamageCap / level;
    }

    public static float getDamageCapAmount(int level, float damage)
    {
        return Mth.clamp(damage, 0, (getCapAmount(level)));
    }

    @SubscribeEvent
    public static void damageCapEvent(LivingIncomingDamageEvent event)
    {
        if (DTEConfig.enableDamageCap)
        {
            var entity = event.getEntity();
            var source = event.getSource();
            var effect = entity.getEffect(DTEPotionEffectRegistry.ABRACADABRA_EFFECT);

            // Should prevent it from bypassing /kill
            if (effect != null && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            {
                int level = effect.getAmplifier() + 1;
                float baseDamage = event.getAmount();
                float newDamage = getDamageCapAmount(level, baseDamage);

                event.setAmount(newDamage);
                //DiscerningTheEldritch.LOGGER.debug("Old damage: " + baseDamage);
                //DiscerningTheEldritch.LOGGER.debug("Level: " + level);
                //DiscerningTheEldritch.LOGGER.debug("Base cap: " + DTEConfig.abracadabraDamageCap);
                //DiscerningTheEldritch.LOGGER.debug("Math: " + (DTEConfig.abracadabraDamageCap / level));
                //DiscerningTheEldritch.LOGGER.debug("Damage cap: " + getCapAmount(level));
                //DiscerningTheEldritch.LOGGER.debug("Damage caped at: " + newDamage);
                //DiscerningTheEldritch.LOGGER.debug("New damage: " + event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public static void preventHexes(MobEffectEvent.Applicable event)
    {
        if (DTEConfig.enableHexPrevention)
        {
            LivingEntity entity = event.getEntity();
            var hexEffect = event.getEffectInstance().getEffect();
            var effect = entity.getEffect(DTEPotionEffectRegistry.ABRACADABRA_EFFECT);

            if (effect != null && (hexEffect.value().getCategory() == MobEffectCategory.HARMFUL))
            {
                if (hexEffect.is(DTETags.BYPASS_ABRACADABRA))
                {
                    //DiscerningTheEldritch.LOGGER.debug("Cannot prevent this hex");
                    event.setResult(MobEffectEvent.Applicable.Result.APPLY);
                } else
                {
                    //DiscerningTheEldritch.LOGGER.debug("Prevented a hex");
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }
    }
}
