package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.ConjureGaolerSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.EsotericEdgeSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.OtherworldlyPresenceSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.SilenceSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.BoogieWoogieSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.GuardiansGazeSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SpellRegistries {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, DiscerningTheEldritch.MOD_ID);

    public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    /***
     * Eldritch Spells
     */

    // Meteorology - Changes weather or time

    // Apocalypse - Powerful beam-based attack

    // Mind-Flaying - Inflicts insanity and darkness effects

    // Assimilation (Debating on keeping) - On enemy death, turn them into a summon?

    // Silence - Prevent target from casting spells
    public static final Supplier<AbstractSpell> SILENCE = registerSpell(new SilenceSpell());

    // Forsaken Aid - Summons entities like Raise Dead

    // Esoteric Edge - Melee spell
    public static final Supplier<AbstractSpell> ESOTERIC_EDGE = registerSpell(new EsotericEdgeSpell());

    // Wrath of God - Destroy all creatures, they can't regenerate (do a lot of damage, prevent any healing)

    // Conjour Gaoler - Summon the Gaoler entity
    public static final Supplier<AbstractSpell> CONJURE_GAOLER = registerSpell(new ConjureGaolerSpell());

    // Forlorn Blades - Summon several blades around the caster that attack whatever the attack attacks

    // Otherworldly Presence - Teleport based spell, grants you the Metaphysical effect
    public static final Supplier<AbstractSpell> OTHERWORLDLY_PRESENCE = registerSpell(new OtherworldlyPresenceSpell());


    /***
     * Holy Spells
     */

    // Exorcism - Resets insanity


    /***
     * Blood Spells
     */

    // Bloodlust - Damage dealt by user gets turned into health; lifesteal on hit


    /***
     * Lightning Spells
     */

    // Thundering Blow/Lightning Crash - Melee spell


    /***
     * Evocation Spells
     */

    // Boogie Woogie - Switch places with a target
    public static final Supplier<AbstractSpell> BOOGIE_WOOGIE = registerSpell(new BoogieWoogieSpell());

    // Guardian's Gaze - Special version of Ray of Frost that inflicts Mining Fatigue (By request of Ama)
    public static final Supplier<AbstractSpell> GUARDIANS_GAZE = registerSpell(new GuardiansGazeSpell());


    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
