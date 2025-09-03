package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class DTERarities {
    public static final EnumProxy<Rarity> APOTHIC_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "discerning_the_eldritch:apothic",
            (UnaryOperator<Style>) ((style) -> style.withColor(0xba1127))
            );

    public static final EnumProxy<Rarity> GLACIAL_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "discerning_the_eldritch:glacial",
            (UnaryOperator<Style>) ((style) -> style.withColor(0x006aeb))
    );
}
