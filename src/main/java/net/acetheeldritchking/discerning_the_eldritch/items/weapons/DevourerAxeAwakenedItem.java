package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ImmolateEffect;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.networking.DTEAttachmentSync;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttachmentRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.SpellRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DevourerAxeAwakenedItem extends MagicSwordItem implements UniqueItem {
    public static final int COOLDOWN = 15 * 20;

    public DevourerAxeAwakenedItem() {
        super(
                DTEWeaponTiers.DEVOURER_AWAKENED,
                ItemPropertiesHelper.equipment(1).fireResistant().rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue()).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.DEVOURER_AWAKENED)),
                SpellDataRegistryHolder.of(
                        new SpellDataRegistryHolder(SpellRegistries.BLADES_OF_RANCOR, 6))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown())
        {
            LivingEntity attacker = MinecraftInstanceHelper.getPlayer();

            tooltipComponents.add(
                    Component.translatable(
                            "tooltip.irons_spellbooks.passive_ability",
                            Component.literal(Utils.timeFromTicks(COOLDOWN, 1)).withStyle(ChatFormatting.LIGHT_PURPLE)
                    ).withStyle(ChatFormatting.DARK_PURPLE)
            );
            tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".desc")).withStyle(ChatFormatting.YELLOW));
            tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".desc2").append(devourStacks(attacker)).
                    withStyle(ChatFormatting.DARK_PURPLE).
                    withStyle(ChatFormatting.ITALIC));
        } else
        {
            tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.more_details").withStyle(ChatFormatting.GRAY));
        }
    }

    private static String devourStacks(LivingEntity entity)
    {
        entity.getData(DTEAttachmentRegistry.DEVOURED_ENTITIES);
        int stacks = entity.getData(DTEAttachmentRegistry.DEVOURED_ENTITIES.get());

        return String.format("%2d", stacks);
    }
}
