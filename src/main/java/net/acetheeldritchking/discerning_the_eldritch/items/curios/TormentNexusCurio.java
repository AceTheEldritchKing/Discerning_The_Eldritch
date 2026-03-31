package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;

public class TormentNexusCurio extends SimpleDescriptiveCurio {
    public TormentNexusCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()).fireResistant(), null);
    }
}
