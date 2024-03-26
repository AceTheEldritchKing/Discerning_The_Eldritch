package net.acetheeldritchking.discerning_the_eldritch.registeries;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
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

    // ELDRITCH SPELLS //

    // Meteorology

    // Apocalypse

    // Atomic Annihilation

    // Mind-Flaying

    // Subjugation

    // Forsaken Aid

    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
