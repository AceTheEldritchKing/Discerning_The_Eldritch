package net.acetheeldritchking.discerning_the_eldritch.items.spellbooks;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BlackBookSpellbook extends UniqueSpellBook {
    public BlackBookSpellbook() {
        super(SpellDataRegistryHolder.of
                (
                        new SpellDataRegistryHolder(SpellRegistry.SCULK_TENTACLES_SPELL, 3),
                        new SpellDataRegistryHolder(SpellRegistry.PLANAR_SIGHT_SPELL, 3),
                        new SpellDataRegistryHolder(SpellRegistry.SACRIFICE_SPELL, 4),
                        new SpellDataRegistryHolder(SpellRegistry.COUNTERSPELL_SPELL, 1)
                ), 6
        );
        withSpellbookAttributes(
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        );
    }
}
