package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.curios.ImbuableCurio;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;

public class AmuletOfSculkTreasureCurio extends ImbuableCurio implements UniqueItem {
    public AmuletOfSculkTreasureCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant(), Curios.NECKLACE_SLOT,
                SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistries.CONJURE_GAOLER, 1)));
    }
}
