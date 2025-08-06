package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class YmirSwordItem extends MagicSwordItem implements UniqueItem {
    public YmirSwordItem() {
        super(
                DTEWeaponTiers.YMIR,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.YMIR)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.ESOTERIC_EDGE, 6),
                        new SpellDataRegistryHolder(SpellRegistries.SILENCE, 6))
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
