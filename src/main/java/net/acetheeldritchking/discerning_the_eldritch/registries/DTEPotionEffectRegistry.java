package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.effects.MetaphysicalPotionEffect;
import net.acetheeldritchking.discerning_the_eldritch.effects.SilencePotionEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DTEPotionEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DiscerningTheEldritch.MOD_ID);

    // Silence Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> SILENCE_POTION_EFFECT = MOB_EFFECTS.register("silence_potion_effect", SilencePotionEffect::new);

    // Metaphysical Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> METAPHYSICAL_POTION_EFFECT = MOB_EFFECTS.register("metaphysical_potion_effect", MetaphysicalPotionEffect::new);

    // Gaoler Timer
    public static final DeferredHolder<MobEffect, SummonTimer> GAOLER_TIMER = MOB_EFFECTS.register("gaoler_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
