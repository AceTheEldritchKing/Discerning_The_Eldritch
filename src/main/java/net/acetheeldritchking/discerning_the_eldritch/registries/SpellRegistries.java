package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.*;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.BoogieWoogieSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.GuardiansGazeSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.ritual.CallAscendedOneSpell;
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

    // Forsaken Aid - Summons entities like Raise Dead; summons The Apostle (spell casting summon), Feral Maw (lowly grunt) and Unyielding Juggernaut (tanky summon)
    public static final Supplier<AbstractSpell> CONJURE_FORSAKE_AID = registerSpell(new ConjureForsakenAidSpell());

    // Esoteric Edge - Melee spell
    public static final Supplier<AbstractSpell> ESOTERIC_EDGE = registerSpell(new EsotericEdgeSpell());

    // Wrath of God - Destroy all creatures, they can't regenerate (do a lot of damage, prevent any healing)

    // Conjour Gaoler - Summon the Gaoler entity
    public static final Supplier<AbstractSpell> CONJURE_GAOLER = registerSpell(new ConjureGaolerSpell());

    // Forlorn Blades - Summon several blades around the caster that attack whatever the attack attacks

    // Otherworldly Presence - Teleport based spell, grants you the Metaphysical effect
    public static final Supplier<AbstractSpell> OTHERWORLDLY_PRESENCE = registerSpell(new OtherworldlyPresenceSpell());

    // Mend Flesh - Healing spell; heals you for a small amount, but grants an effect that'll heal you for X amount every time you attack or gain XP
    public static final Supplier<AbstractSpell> MEND_FLESH = registerSpell(new MendFleshSpell());

    // Rift Walker - Teleport based spell, leaves an unstable rift at your original location and at your new location. Rift deals damage

    // Entangled - Prevents the user from being counterspelled at the cost of reduced spell power

    // Subjugation - AoE Silence that damages targets, prevents them from doing anything of their agency, damages them for doing anything, stops healing, stops mana regen, blindness and darkness, weakness and reduced armor, more suseptible to spells,
    // Yeah this is cancerous for a reason, this won't be casted by a player, only by a boss


    /***
     * Ritual Spells
     */
    // Call: Ascended One
    public static final Supplier<AbstractSpell> CALL_ASCENDED_ONE = registerSpell(new CallAscendedOneSpell());


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

    // Lightning Step/Thunder Step - Movement spell, burning dash-like spell. Short distance but recastable


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
