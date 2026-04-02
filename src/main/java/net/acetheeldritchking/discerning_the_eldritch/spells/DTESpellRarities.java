package net.acetheeldritchking.discerning_the_eldritch.spells;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum DTESpellRarities implements StringRepresentable {
    COMMON(0),
    UNCOMMON(1),
    RARE(2),
    EPIC(3),
    LEGENDARY(4),
    ANCIENT(5),
    APOCRYPHAL(6)
    ;

    private final int value;
    public static final Codec<DTESpellRarities> CODEC = StringRepresentable.fromEnum(DTESpellRarities::values);

    DTESpellRarities(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    public MutableComponent getDisplayName()
    {
        return DISPLAYS[getValue()];
    }

    private static List<Double> dteRawRarityConfig;
    private static List<Double> dteRarityConfig = null;

    public static List<Double> getDteRawRarityConfig()
    {
        if (dteRarityConfig == null)
        {
            dteRawRarityConfig = DTESpellRarities.getRawDTERarityConfigInternal();
        }

        return dteRawRarityConfig;
    }

    private static List<Double> getRawDTERarityConfigInternal()
    {
        var fromConfig = (List<Double>) ServerConfigs.RARITY_CONFIG.get();

        if (fromConfig.size() != 5)
        {
            var defaultConfig = (List<Double>) ServerConfigs.RARITY_CONFIG.getDefault();
            DiscerningTheEldritch.LOGGER.info("INVALID RARITY CONFIG FOUND (Size != 5): {} FALLING BACK TO DEFAULT: {}", fromConfig, defaultConfig);
            return defaultConfig;
        }

        if (fromConfig.stream().mapToDouble(a -> a).sum() != 1)
        {
            var defaultConfig = (List<Double>) ServerConfigs.RARITY_CONFIG.getDefault();
            DiscerningTheEldritch.LOGGER.info("INVALID RARITY CONFIG FOUND (Values must add up to 1): {} FALLING BACK TO DEFAULT: {}", fromConfig, defaultConfig);
            return defaultConfig;
        }

        return fromConfig;
    }

    public static List<Double> getDteRarityConfig()
    {
        if (dteRarityConfig == null)
        {
            var counter = new AtomicDouble();
            dteRarityConfig = new ArrayList<>();

            getDteRawRarityConfig().forEach(
                    item -> {
                        dteRarityConfig.add(counter.addAndGet(item));
                    });
        }

        return dteRarityConfig;
    }

    public int compareRarity(DTESpellRarities other)
    {
        return Integer.compare(this.getValue(), other.getValue());
    }

    //Tbh I don't think I need rarityTest() but we'll see.

    public ChatFormatting getChatFormatting()
    {
        return switch (this)
        {
            case COMMON -> ChatFormatting.GRAY;
            case UNCOMMON -> ChatFormatting.GREEN;
            case RARE -> ChatFormatting.AQUA;
            case EPIC -> ChatFormatting.LIGHT_PURPLE;
            case LEGENDARY -> ChatFormatting.GOLD;
            case ANCIENT -> ChatFormatting.BLUE;
            case APOCRYPHAL -> ChatFormatting.DARK_RED;
        };
    }

    private final MutableComponent[] DISPLAYS = {
            Component.translatable("rarity.irons_spellbooks.common").withStyle(ChatFormatting.GRAY),
            Component.translatable("rarity.irons_spellbooks.uncommon").withStyle(ChatFormatting.GREEN),
            Component.translatable("rarity.irons_spellbooks.rare").withStyle(ChatFormatting.AQUA),
            Component.translatable("rarity.irons_spellbooks.epic").withStyle(ChatFormatting.LIGHT_PURPLE),
            Component.translatable("rarity.irons_spellbooks.legendary").withStyle(ChatFormatting.GOLD),
            Component.translatable("rarity.discerning_the_eldritch.ancient").withStyle(ChatFormatting.BLUE),
            Component.translatable("rarity.discerning_the_eldritch.apocryphal").withStyle(ChatFormatting.DARK_RED)
    };

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
