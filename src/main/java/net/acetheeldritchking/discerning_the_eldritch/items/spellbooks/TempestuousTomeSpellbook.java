package net.acetheeldritchking.discerning_the_eldritch.items.spellbooks;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class TempestuousTomeSpellbook extends UniqueSpellBook {
    public TempestuousTomeSpellbook() {
        super(SpellDataRegistryHolder.of(
                new SpellDataRegistryHolder(SpellRegistry.GUST_SPELL, 12),
                new SpellDataRegistryHolder(SpellRegistry.THUNDERSTORM_SPELL, 10)
        ), 8);
        withSpellbookAttributes(new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE));
        withSpellbookAttributes(new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, 0.10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }
}
