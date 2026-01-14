package net.acetheeldritchking.discerning_the_eldritch.compat.pastel;

import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEFluidRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class BucketOfMalice extends BucketItem {
    public BucketOfMalice() {
        super(
                DTEFluidRegistry.LIQUID_MALICE.get(),
                new Item.Properties().craftRemainder(Items.BUCKET).rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue()).stacksTo(1)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.discerning_the_eldritch.bucket_of_malice.description").
                withStyle(ChatFormatting.GRAY));
    }
}
