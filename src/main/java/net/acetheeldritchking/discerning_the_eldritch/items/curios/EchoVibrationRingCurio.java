package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;

public class EchoVibrationRingCurio extends SimpleDescriptiveCurio {
    public EchoVibrationRingCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant(), Curios.RING_SLOT);
    }
}
