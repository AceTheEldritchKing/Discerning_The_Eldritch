package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.curios.PresetImbueCurio;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;

public class AmuletOfSculkTreasureCurio extends PresetImbueCurio implements UniqueItem {
    public AmuletOfSculkTreasureCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(ASRarities.SCULK_RARITY_PROXY.getValue()), Curios.NECKLACE_SLOT,
                SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistries.CONJURE_GAOLER, 1)));
    }
}
