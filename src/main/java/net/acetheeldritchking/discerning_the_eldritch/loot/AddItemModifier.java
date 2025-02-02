package net.acetheeldritchking.discerning_the_eldritch.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemModifier>> CODEC = Suppliers.memoize(()
    -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            Codec.STRING.fieldOf("loot_table").forGetter(m -> m.lootTable)).apply(inst, AddItemModifier::new)));
    private final String lootTable;

    protected AddItemModifier(LootItemCondition[] conditionsIn, String item) {
        super(conditionsIn);
        this.lootTable = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        ResourceLocation resourcePath = ResourceLocation.parse(lootTable);
        var addedLootTable = lootContext.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, resourcePath));

        addedLootTable.getRandomItemsRaw(lootContext, generatedLoot::add);

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
