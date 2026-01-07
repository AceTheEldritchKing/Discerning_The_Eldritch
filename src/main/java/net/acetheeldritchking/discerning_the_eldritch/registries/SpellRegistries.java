package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.spells.blood.VeinRipperSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.*;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.BoogieWoogieSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.GuardiansGazeSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.fire.SoulSetAblazeSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.fire.SoulSliceSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.holy.ExorcismSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.ice.CrystallineCarverSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.ice.GlacialEdgeSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.ritual.*;
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
    public static final Supplier<AbstractSpell> RIFT_WALKER = registerSpell(new RiftWalkerSpell());

    // Entangled - Prevents the user from being counterspelled at the cost of reduced spell power

    // Subjugation - AoE Silence that damages targets, prevents them from doing anything of their agency, damages them for doing anything, stops healing, stops mana regen, blindness and darkness, weakness and reduced armor, more suseptible to spells,
    // Yeah this is cancerous for a reason, this won't be casted by a player, only by a boss

    // Abracadabra - Prevents the user from being hexed. Prevents any fatal damage from being taken (configurable)
    public static final Supplier<AbstractSpell> ABRACADABRA = registerSpell(new AbracadabraSpell());

    // Esoteric Strike - Harness raw eldritch mana into an empowered strike. Damage scales based on melee damage
    public static final Supplier<AbstractSpell> ESOTERIC_STRIKE = registerSpell(new EsotericStrikeSpell());

    // Eternal Chains - Wrap the target in chains, preventing movement. The chains cannot be disspelled & the target cannot teleport. Any damage towards the target is halved.

    // Malevolent Maelstrom - Conjure negative energy around the caster, manifesting in a swirling storm of Eso Edges slicing any nearby entities. Spell can be targeted as well to deal triple damage to a singular target. Scales with Fire SP

    // Abhorrent Inferno - Channel your mana into an eruption of Eldritch magic, swinging your weapon around into a fatal dance. Entities caught in your wicked ballet will be affected with the Abhorrent Burn debuff. At the end of your dance, your mana regeneration is halted for a few seconds. You may target another entity to "dance" with you, increasing the damage of your slashes

    // Precipice Blades - Conjure blades from the ground to skewer entities. Entities hit are weaker to spell damage

    // Conjure: Arcadian Stag - Conjure a stag from Arcadia itself. The spell scales off Eldritch & Occult SP. The stag can walk on fluids

    // Umbral Miasma - Create a field of miasma that disorients targets and leaves the susceptible to Eldritch spells

    // Shadow Splitter - Send out a blade of shadow that homes in on the nearest target, cutting through them repeatedly

    // Knight's Edge - Conjure a group of shadow blades with down upon a selected area. Blades cause hit entities to take more Eldritch damage

    // Dark Rise - Recast; On first recast, ascend into the air. On the second, slam down on your opponent

    // WATEOA - If you know, you know.

    // Black Sun - If you know, you know.


    /***
     * Ender Spells
     */

    // Hocus Pocus - Tether to an entity on the first recast. On the second, summon that entity to your position


    /***
     * Blood Spells
     */

    // Bloodlust - Damage dealt by user gets turned into health; lifesteal on hit

    // Vein Ripper - Blood melee spell, slash upwards, inflicting blood rot on hit and giving the caster vigor
    public static final Supplier<AbstractSpell> VEIN_RIPPER = registerSpell(new VeinRipperSpell());


    /***
     * Evocation Spells
     */

    // Boogie Woogie - Switch places with a target
    public static final Supplier<AbstractSpell> BOOGIE_WOOGIE = registerSpell(new BoogieWoogieSpell());

    // Guardian's Gaze - Special version of Ray of Frost that inflicts Mining Fatigue (By request of Ama)
    public static final Supplier<AbstractSpell> GUARDIANS_GAZE = registerSpell(new GuardiansGazeSpell());


    /***
     * Fire Spells
     */

    // Soul Slice
    public static final Supplier<AbstractSpell> SOUL_SLICE = registerSpell(new SoulSliceSpell());

    // Soul Set Ablaze
    public static final Supplier<AbstractSpell> SOUL_SET_ABLAZE = registerSpell(new SoulSetAblazeSpell());

    // Brimstone Apocalypse - Summon a storm of brimstone meteors from the sky. At the end of the spell, summon a giant meteor that does massive damage + griefing (including to the caster). Scales with Eldritch SP


    /***
     * Holy Spells
     */

    // Exorcism - Resets insanity
    public static final Supplier<AbstractSpell> EXORCISM = registerSpell(new ExorcismSpell());

    /***
     * Ice Spells
     */

    // Glacial Edge (Low damage projectile for crowd control & inflicts Chilled)
    public static final Supplier<AbstractSpell> GLACIAL_EDGE = registerSpell(new GlacialEdgeSpell());

    // Crystalline Carver
    public static final Supplier<AbstractSpell> CRYSTALLINE_CARVER = registerSpell(new CrystallineCarverSpell());

    // Ice Shock (A quick projectile that freezes an entity completely)


    /***
     * Lightning Spells
     */

    // Thundering Blow/Lightning Crash - Melee spell

    // Lightning Step/Thunder Step - Movement spell, burning dash-like spell. Short distance but recastable


    /***
     * Ritual Spells
     */

    // Call: Ascended One
    public static final Supplier<AbstractSpell> CALL_ASCENDED_ONE = registerSpell(new CallAscendedOneSpell());

    // Blades of Rancor - Summon a set of blades that home in where ever the caster is looking
    public static final Supplier<AbstractSpell> BLADES_OF_RANCOR = registerSpell(new BladesOfRancorSpell());

    // Zealous Harbinger - Summon smaller variants of the Cataclysm blade in front of the caster which impales the target
    public static final Supplier<AbstractSpell> ZEALOUS_HARBINGER = registerSpell(new ZealousHarbingerSpell());

    // Ravenous Revenant - Unleash a mighty roar, nearby entities are marked as Prey. Hitting Prey entities causes a ravenous maw to lash at them
    public static final Supplier<AbstractSpell> RAVENOUS_REVENANT = registerSpell(new RavenousRevenantSpell());

    // Libra's Judgement - Target an entity; if they are above 50% health, a larger version of Mourning Star slams down on them. If they are below 50% health, they are immolated with vicious flames
    public static final Supplier<AbstractSpell> LIBRAS_JUDGEMENT = registerSpell(new LibrasJudgementSpell());

    // Transmutation - A generic spell used for transmutating certain materials for crafting


    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
