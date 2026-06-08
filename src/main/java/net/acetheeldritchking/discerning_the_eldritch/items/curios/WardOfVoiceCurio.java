package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.utils.compat.DTECurioSlots;
import net.minecraft.world.item.Rarity;

public class WardOfVoiceCurio extends SimpleDescriptiveCurio {
    public WardOfVoiceCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(Rarity.RARE), DTECurioSlots.WARD_SLOT);
    }
}
