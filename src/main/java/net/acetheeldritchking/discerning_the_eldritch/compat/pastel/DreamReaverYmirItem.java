package net.acetheeldritchking.discerning_the_eldritch.compat.pastel;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Unbreakable;

import java.util.List;

public class DreamReaverYmirItem extends MagicSwordItem implements UniqueItem {
    public DreamReaverYmirItem() {
        super(
                PastelCompatWeaponTiers.DREAM_REAVER_YMIR,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(PastelCompatWeaponTiers.DREAM_REAVER_YMIR)).component(DataComponents.UNBREAKABLE, new Unbreakable(false)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.ESOTERIC_EDGE, 6),
                        new SpellDataRegistryHolder(SpellRegistries.SILENCE, 6))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.ymir.description").
                withStyle(ChatFormatting.DARK_RED).
                withStyle(ChatFormatting.ITALIC));
    }
}
