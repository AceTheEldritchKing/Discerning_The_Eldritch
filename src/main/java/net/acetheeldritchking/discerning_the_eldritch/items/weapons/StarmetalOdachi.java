package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;

public class StarmetalOdachi extends ExtendedSwordItem {
    public StarmetalOdachi() {
        super(
                DTEWeaponTiers.STARMETAL_ODACHI,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(DTERarities.STARDUST_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.STARMETAL_ODACHI))
        );
    }
}
