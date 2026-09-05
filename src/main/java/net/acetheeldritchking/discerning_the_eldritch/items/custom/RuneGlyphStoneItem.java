package net.acetheeldritchking.discerning_the_eldritch.items.custom;

import io.redspace.ironsspellbooks.item.ReadableLoreItem;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.List;

public class RuneGlyphStoneItem extends ReadableLoreItem {
    private final boolean translated;

    public RuneGlyphStoneItem(boolean translated, Properties pProperties) {
        super(DiscerningTheEldritch.id("textures/entity/lectern/glyph_stone_lectern.png"), pProperties);
        this.translated = translated;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (translated)
        {
            tooltipComponents.add(Component.translatable("tooltip.irons_spellbooks.translated").withStyle(ChatFormatting.YELLOW));
        } else
        {
            tooltipComponents.add(Component.translatable("tooltip.irons_spellbooks.untranslated").withStyle(ChatFormatting.RED));
        }
    }

    public static WrittenBookContent TRANSLATED_CONTENTS = new WrittenBookContent(Filterable.passThrough(""), "Unknown", 0,
            List.of(
                    Filterable.passThrough(Component.translatable("item.discerning_the_eldritch.glyph_stone.header").append("\n\n").append(Component.translatable("item.discerning_the_eldritch.glyph_stone.alphabet")))
            ), true);
    public static WrittenBookContent UNTRANSLATED_CONTENTS = new WrittenBookContent(Filterable.passThrough(""), "Unknown", 0,
            List.of(
                    Filterable.passThrough(Component.translatable("item.discerning_the_eldritch.glyph_stone.header").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("illageralt"))).append(Component.literal("\n\n").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("default")))).append(Component.translatable("item.discerning_the_eldritch.glyph_stone.alphabet").withStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "archaicalt")))))
            ), true);
}
