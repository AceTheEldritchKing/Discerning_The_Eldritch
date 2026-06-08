package net.acetheeldritchking.discerning_the_eldritch.items.curios;

import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.utils.compat.DTECurioSlots;
import net.minecraft.world.item.Rarity;

public class WardOfClarityCurio extends SimpleDescriptiveCurio {
    public WardOfClarityCurio() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).fireResistant().rarity(Rarity.EPIC), DTECurioSlots.WARD_SLOT);
    }
}
