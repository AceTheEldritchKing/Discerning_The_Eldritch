package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.compat.CompatManager;
import net.acetheeldritchking.discerning_the_eldritch.compat.pastel.PastelCompatItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
                        output.accept(ItemRegistries.RITUAL_RUNE.get());
                        output.accept(ItemRegistries.ELDRITCH_UPGRADE_ORB.get());
                        output.accept(ItemRegistries.RITUAL_UPGRADE_ORB.get());
                        output.accept(ItemRegistries.ELDRITCH_UPGRADE_TEMPLATE.get());
                        output.accept(ItemRegistries.ELDRITCH_SOUL_SHARD.get());
                        output.accept(ItemRegistries.SHARD_OF_MALICE.get());
                        output.accept(ItemRegistries.BOTTLE_OF_LIQUID_MALICE.get());
                        output.accept(ItemRegistries.FORBIDDEN_SPELL_IMPROVEMENT.get());
                        output.accept(FurledMapItem.of(DiscerningTheEldritch.id("cultist_base"),
                                ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace("overworld")), Component.translatable("item.discerning_the_eldritch.cultist_base_map"), true));
                        // Curios
                        output.accept(ItemRegistries.ECHO_VIBRATION_RING.get());
                        output.accept(ItemRegistries.AMULET_OF_SCULK_TREASURE.get());
                        output.accept(ItemRegistries.CASTERS_MANTLE.get());
                        output.accept(ItemRegistries.IRONBOUND_FEATHER.get());
                        output.accept(ItemRegistries.PYRIUM_SHEATH.get());
                        output.accept(ItemRegistries.FROSTBOURNE_SHEATH.get());
                        output.accept(ItemRegistries.RAZOR_SHEATH.get());
                        output.accept(ItemRegistries.RIFT_RIPPER_EMBLEM.get());
                        //output.accept(ItemRegistries.KINGS_EFFIGY.get());
                        // Treasure
                        output.accept(ItemRegistries.APOTHIC_KEY.get());
                        output.accept(ItemRegistries.ASCENDED_KEY.get());
                        output.accept(ItemRegistries.RITUAL_KEY.get());
                        output.accept(ItemRegistries.FADING_SCULK_LANTERN.get());
                        // Spellbooks
                        output.accept(ItemRegistries.BLACK_BOOK_SPELLBOOK.get());
                        output.accept(ItemRegistries.THE_APOCRYPHA_SPELLBOOK.get());
                        output.accept(ItemRegistries.TEMPESTUOUS_TOME.get());
                        //output.accept(ItemRegistries.FROZEN_FOLIO.get());
                        output.accept(ItemRegistries.DIARY_OF_DECAY.get());
                        //output.accept(ItemRegistries.GUARDIANS_GAZE.get());
                        // Weapons
                        output.accept(ItemRegistries.DEEP_GREATSWORD.get());
                        output.accept(ItemRegistries.GOD_SPEAR.get());
                        output.accept(ItemRegistries.FORSAKEN_FLAMBERGE.get());
                        output.accept(ItemRegistries.ICE_SPEAR.get());
                        output.accept(ItemRegistries.SNOWGRAVE.get());
                        output.accept(ItemRegistries.SOUL_FIRE_SCYTHE.get());
                        output.accept(ItemRegistries.RITUAL_DAGGER.get());
                        output.accept(ItemRegistries.FORLORN_RAPIER.get());
                        output.accept(ItemRegistries.BROKEN_LEGENDS_BLADE.get());
                        output.accept(ItemRegistries.CATACLYSM.get());
                        output.accept(ItemRegistries.DEVOURER.get());
                        output.accept(ItemRegistries.MOURNING_STAR.get());
                        output.accept(ItemRegistries.CATACLYSM_AWAKENED.get());
                        output.accept(ItemRegistries.DEVOURER_AWAKENED.get());
                        output.accept(ItemRegistries.MOURNING_STAR_AWAKENED.get());
                        output.accept(ItemRegistries.YMIR.get());
                        // Staves
                        output.accept(ItemRegistries.STAFF_OF_VEHEMENCE.get());
                        output.accept(ItemRegistries.STAFF_OF_ASCENSION.get());
                        output.accept(ItemRegistries.BROKEN_LEGENDS_STAFF.get());
                        // Armor
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_HOOD.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_MASK.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_HELMET.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_ROBES.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_LEGGINGS.get());
                        output.accept(ItemRegistries.ELDRITCH_WARLOCK_GREAVES.get());

                        output.accept(ItemRegistries.CRIMSON_STAG_ANTLERS.get());
                        output.accept(ItemRegistries.CRIMSON_STAG_ROBES.get());
                        output.accept(ItemRegistries.CRIMSON_STAG_LEGGINGS.get());
                        output.accept(ItemRegistries.CRIMSON_STAG_BOOTS.get());
                        // Spawn Eggs
                        output.accept(ItemRegistries.ACOLYTE_SPAWN_EGG.get());
                        output.accept(ItemRegistries.SUMMONER_SPAWN_EGG.get());
                        output.accept(ItemRegistries.CRUSADER_SPAWN_EGG.get());
                        output.accept(ItemRegistries.ASCENDED_CULTIST_SPAWN_EGG.get());
                        output.accept(ItemRegistries.BLOOD_CULTIST_CAPTAIN_SPAWN_EGG.get());
                        output.accept(ItemRegistries.BLOOD_CULTIST_MAGE_SPAWN_EGG.get());
                        output.accept(ItemRegistries.BLOOD_CULTIST_WITCH_SPAWN_EGG.get());
                        output.accept(ItemRegistries.BLOOD_MATRIARCH_SPAWN_EGG.get());
                        output.accept(ItemRegistries.ELECTROMANCER_SPAWN_EGG.get());

                        // Compat
                        if (CompatManager.isPastelLoaded())
                        {
                            // Weapons
                            output.accept(PastelCompatItems.DREAM_REAVER_YMIR.get());
                        }
                    }).build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
