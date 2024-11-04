package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class YmirSwordItem extends MagicSwordItem {
    public YmirSwordItem(SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(
                DTEWeaponTiers.YMIR,
                ItemPropertiesHelper.equipment().fireResistant().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.YMIR)
                ), spellDataRegistryHolders
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.ymir_description").
                withStyle(ChatFormatting.DARK_RED).
                withStyle(ChatFormatting.ITALIC));
    }
}
