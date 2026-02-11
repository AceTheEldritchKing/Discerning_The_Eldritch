package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.effects.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DTEPotionEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DiscerningTheEldritch.MOD_ID);

    // Silence Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> SILENCE_POTION_EFFECT = MOB_EFFECTS.register("silence_potion_effect", SilencePotionEffect::new);

    // Metaphysical Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> METAPHYSICAL_POTION_EFFECT = MOB_EFFECTS.register("metaphysical_potion_effect", MetaphysicalPotionEffect::new);

    // Mend Flesh Effect
    public static final DeferredHolder<MobEffect, MobEffect> MEND_FLESH_EFFECT = MOB_EFFECTS.register("mend_flesh_potion_effect", MendFleshPotionEffect::new);

    // Abracadabra Potion Effect
    public static final DeferredHolder<MobEffect, MobEffect> ABRACADABRA_EFFECT = MOB_EFFECTS.register("abracadabra_potion_effect", AbracadabraPotionEffect::new);

    // Portent Effect
    public static final DeferredHolder<MobEffect, MobEffect> PORTENT_EFFECT = MOB_EFFECTS.register("portent_effect", PortentEffect::new);

    // Frostbite Effect
    public static final DeferredHolder<MobEffect, MobEffect> FROSTBITE_EFFECT = MOB_EFFECTS.register("frostbite_effect", FrostbitePotionEffect::new);

    // Malignant Burn Effect
    public static final DeferredHolder<MobEffect, MobEffect> MALIGNANT_BURN_EFFECT = MOB_EFFECTS.register("malignant_burn_potion_effect", MalignantBurnEffect::new);

    // Ruin Effect

    // Accursed Effect
    public static final DeferredHolder<MobEffect, MobEffect> ACCURSED_EFFECT = MOB_EFFECTS.register("accursed_potion_effect", AccursedPotionEffect::new);

    // Blood Rot Effect
    public static final DeferredHolder<MobEffect, MobEffect> BLOOD_ROT_EFFECT = MOB_EFFECTS.register("blood_rot_potion_effect", BloodRotPotionEffect::new);

    // Devoured Effect

    // Prey Effect
    public static final DeferredHolder<MobEffect, MobEffect> PREY_POTION_EFFECT = MOB_EFFECTS.register("prey_potion_effect", PreyPotionEffect::new);

    // Predator Effect
    public static final DeferredHolder<MobEffect, MobEffect> PREDATOR_POTION_EFFECT = MOB_EFFECTS.register("predator_potion_effect", PredatorPotionEffect::new);

    // Scorched Soul Effect
    public static final DeferredHolder<MobEffect, MobEffect> SCORCHED_SOUL_EFFECT = MOB_EFFECTS.register("scorched_soul_potion_effect", ScorchedSoulPotionEffect::new);

    // Stardusted Effect
    public static final DeferredHolder<MobEffect, MobEffect> STARDUSTED_EFFECT = MOB_EFFECTS.register("star_dusted_potion_effect", StardustedPotionEffect::new);

    // Ruin Effect
    public static final DeferredHolder<MobEffect, MobEffect> RUIN_EFFECT = MOB_EFFECTS.register("ruin_potion_effect", RuinPotionEffect::new);


    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
