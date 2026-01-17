package net.acetheeldritchking.discerning_the_eldritch.items.custom;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.custom.LootBagItem;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class AscendedOneLootbag extends LootBagItem {
    static ResourceLocation lootTable = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "entities/ascended_one_lootbag");

    public AscendedOneLootbag() {
        super(
                ItemPropertiesHelper.equipment(8).fireResistant().rarity(ASRarities.COSMIC_RARITY_PROXY.getValue()),
                lootTable,
                ParticleTypes.ENCHANT
        );
    }
}
