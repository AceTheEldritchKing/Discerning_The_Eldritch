package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DTECreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DiscerningTheEldritch.MOD_ID);

    public static final Supplier<CreativeModeTab> DTE_ITEMS_TAB = CREATIVE_MODE_TAB.register("dte_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemRegistries.CORRUPTED_CLOTH.get()))
                    .title(Component.translatable("creative_tab.discerning_the_eldritch.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemRegistries.CORRUPTED_CLOTH.get());
                        output.accept(ItemRegistries.ECHO_VIBRATION_RING.get());
                        output.accept(ItemRegistries.BLACK_BOOK_SPELLBOOK.get());
                        output.accept(ItemRegistries.THE_APOCRYPHA_SPELLBOOK.get());
                    }).build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
