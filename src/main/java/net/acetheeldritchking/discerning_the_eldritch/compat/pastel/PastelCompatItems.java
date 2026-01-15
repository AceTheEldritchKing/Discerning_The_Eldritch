package net.acetheeldritchking.discerning_the_eldritch.compat.pastel;

import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEFluidRegistry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class PastelCompatItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DiscerningTheEldritch.MOD_ID);

    // Dream Reaver Ymir
    public static final DeferredHolder<Item, Item> DREAM_REAVER_YMIR = ITEMS.register("dream_reaver_ymir", DreamReaverYmirItem::new);

    // Bucket of Malice
    public static final DeferredHolder<Item, Item> BUCKET_OF_MALICE = ITEMS.register("bucket_of_malice",
            () -> new BucketItem((Fluid) DTEFluidRegistry.LIQUID_MALICE.get(), new Item.Properties().craftRemainder(Items.BUCKET).rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue()).stacksTo(1)));

    public static Collection<DeferredHolder<Item, ? extends Item>> getDTEPastelItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
