package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.effects.MendFleshPotionEffect;
import net.acetheeldritchking.discerning_the_eldritch.effects.MetaphysicalPotionEffect;
import net.acetheeldritchking.discerning_the_eldritch.effects.SilencePotionEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DTEPotionEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DiscerningTheEldritch.MOD_ID);

    // Silence Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> SILENCE_POTION_EFFECT = MOB_EFFECTS.register("silence_potion_effect", SilencePotionEffect::new);

    // Metaphysical Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> METAPHYSICAL_POTION_EFFECT = MOB_EFFECTS.register("metaphysical_potion_effect", MetaphysicalPotionEffect::new);

    // Gaoler Timer
    public static final DeferredHolder<MobEffect, SummonTimer> GAOLER_TIMER = MOB_EFFECTS.register("gaoler_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));

    // Forsaken Aid Timer
    public static final DeferredHolder<MobEffect, SummonTimer> FORSAKEN_TIMER = MOB_EFFECTS.register("forsaken_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));

    // Mend Flesh Effect
    public static final DeferredHolder<MobEffect, MobEffect> MEND_FLESH_EFFECT = MOB_EFFECTS.register("mend_flesh_potion_effect", MendFleshPotionEffect::new);

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
