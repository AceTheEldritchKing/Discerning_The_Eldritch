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
                        // Materials
                        output.accept(ItemRegistries.CORRUPTED_CLOTH.get());
                        output.accept(ItemRegistries.ELDRITCH_RUNE.get());
                        output.accept(ItemRegistries.ELDRITCH_UPGRADE_ORB.get());
                        // Curios
                        output.accept(ItemRegistries.ECHO_VIBRATION_RING.get());
                        //output.accept(ItemRegistries.KINGS_EFFIGY.get());
                        // Spellbooks
                        output.accept(ItemRegistries.BLACK_BOOK_SPELLBOOK.get());
                        output.accept(ItemRegistries.THE_APOCRYPHA_SPELLBOOK.get());
                        output.accept(ItemRegistries.TEMPESTUOUS_TOME.get());
                        output.accept(ItemRegistries.FROZEN_FOLIO.get());
                        output.accept(ItemRegistries.DIARY_OF_DECAY.get());
                        output.accept(ItemRegistries.GUARDIANS_GAZE.get());
                        // Weapons
                        output.accept(ItemRegistries.DEEP_GREATSWORD.get());
                        output.accept(ItemRegistries.GOD_SPEAR.get());
                        output.accept(ItemRegistries.YMIR.get());
                        //output.accept(ItemRegistries.ICE_SPEAR.get());
                        // Armor
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_HOOD.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_MASK.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_HELMET.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_ROBES.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_LEGGINGS.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_GREAVES.get());
                    }).build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
