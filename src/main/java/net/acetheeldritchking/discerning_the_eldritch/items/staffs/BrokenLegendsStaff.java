package net.acetheeldritchking.discerning_the_eldritch.items.staffs;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BrokenLegendsStaff extends StaffItem {
    public BrokenLegendsStaff() {
        super(ItemPropertiesHelper.equipment(1).fireResistant().rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEStaffTier.BROKEN_LEGENDS_STAFF)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.broken_legends_staff_description").
                withStyle(ChatFormatting.DARK_GRAY).
                withStyle(ChatFormatting.ITALIC));
    }
}
