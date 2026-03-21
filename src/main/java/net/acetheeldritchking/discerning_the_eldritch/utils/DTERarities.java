package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class DTERarities {
    // Apothic Rarity
    public static final EnumProxy<Rarity> APOTHIC_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "discerning_the_eldritch:apothic",
            (UnaryOperator<Style>) ((style) -> style.withColor(0xba1127))
            );

    // Divine Rarity
    public static final EnumProxy<Rarity> DIVINE_RARITY_PROXY = new EnumProxy<>(Rarity.class,
            -1,
            "discerning_the_eldritch:divine",
            (UnaryOperator<Style>) ((style) -> style.withColor(0xffe68f))
    );

    // Stardusted Rarity

    // Soul-Burned Rarity
}
