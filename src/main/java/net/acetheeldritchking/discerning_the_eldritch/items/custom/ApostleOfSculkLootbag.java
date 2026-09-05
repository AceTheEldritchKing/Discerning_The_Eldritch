package net.acetheeldritchking.discerning_the_eldritch.items.custom;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.items.custom.LootBagItem;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class ApostleOfSculkLootbag extends LootBagItem {
    static ResourceLocation lootTable = ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "entities/apostle_of_sculk_lootbag");

    public ApostleOfSculkLootbag() {
        super(
                ItemPropertiesHelper.equipment(8).fireResistant().rarity(ASRarities.SCULK_RARITY_PROXY.getValue()),
                lootTable,
                ParticleTypes.SCULK_SOUL
        );
    }
}
