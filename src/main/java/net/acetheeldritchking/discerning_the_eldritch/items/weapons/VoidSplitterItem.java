package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;

public class VoidSplitterItem extends MagicSwordItem implements UniqueItem {
    public VoidSplitterItem() {
        super(
                DTEWeaponTiers.VOIDSPLITTER,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(ASRarities.COSMIC_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.MOURNING_STAR_AWAKENED)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.VOID_SPLITTER, 1))
        );
    }
}
