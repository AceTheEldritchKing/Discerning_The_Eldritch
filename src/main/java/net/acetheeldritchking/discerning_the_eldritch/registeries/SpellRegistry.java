package net.acetheeldritchking.discerning_the_eldritch.registeries;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.spells.eldritch.SilenceSpell;
import net.acetheeldritchking.discerning_the_eldritch.spells.evocation.BoogieWoogieSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY, DiscerningTheEldritch.MOD_ID);

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
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
    public static final RegistryObject<AbstractSpell> SILENCE = registerSpell(new SilenceSpell());

    // Forsaken Aid - Summons entities like Raise Dead

    // Eldritch Slash - Melee spell

    // Wrath of God - Destroy all creatures, they can't regenerate (do a lot of damage, prevent any healing)

    // Conjour Gaoler - Unobtainable spell, summon the Gaoler entity


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
    public static final RegistryObject<AbstractSpell> BOOGIE_WOOGIE = registerSpell(new BoogieWoogieSpell());

    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
