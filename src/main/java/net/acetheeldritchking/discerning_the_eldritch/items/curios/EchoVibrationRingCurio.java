package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;

public class EchoVibrationRingCurio extends SimpleDescriptiveCurio {
    public EchoVibrationRingCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(ASRarities.SCULK_RARITY_PROXY.getValue()).fireResistant(), Curios.RING_SLOT);
    }
}
