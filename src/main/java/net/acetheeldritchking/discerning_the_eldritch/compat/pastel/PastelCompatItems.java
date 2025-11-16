package net.acetheeldritchking.discerning_the_eldritch.compat.pastel;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class PastelCompatItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DiscerningTheEldritch.MOD_ID);

    // Dream Reaver Ymir
    public static final DeferredHolder<Item, Item> DREAM_REAVER_YMIR = ITEMS.register("dream_reaver_ymir", DreamReaverYmirItem::new);

    public static Collection<DeferredHolder<Item, ? extends Item>> getDTEItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
