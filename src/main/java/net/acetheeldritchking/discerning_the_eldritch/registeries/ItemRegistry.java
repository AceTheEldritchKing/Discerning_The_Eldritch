package net.acetheeldritchking.discerning_the_eldritch.registeries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DiscerningTheEldritch.MOD_ID);

    //             //
    // SPELL BOOKS //
    //             //

    // Black Book
    public static final RegistryObject<Item> BLACK_BOOK_SPELLBOOK = ITEMS.register
            ("black_book_spellbook", () -> new SimpleAttributeSpellBook
                    (10, SpellRarity.EPIC, AttributeRegistry.ELDRITCH_SPELL_POWER.get(), 0.10));

    // The Apocrypha
    public static final RegistryObject<Item> THE_APOCRYPHA_SPELLBOOK = ITEMS.register
            ("the_apocrypha_spellbook", () -> new SimpleAttributeSpellBook
                    (12, SpellRarity.LEGENDARY, AttributeRegistry.ELDRITCH_SPELL_POWER.get(), 0.25));


    //                    //
    // STAFFS & GAUNTLETS //
    //                    //

    // Hand of Apocrypha - Gauntlet


    //         //
    // WEAPONS //
    //         //


    //           //
    // MOB DROPS //
    //           //

    // Corrupted Cloth


    //               //
    // GENERIC ITEMS //
    //               //

    // Inert Steel


    //       //
    // ARMOR //
    //       //

    // Eldritch Accoutrement

    // Mage Hunter Armor

    // Pryomancer Warlock Armor

    // Cryomancer Warlock Armor

    // Electromancer Warlock Armor

    // Eternal Druid Warlock Armor

    // Sanguine Cultist Warlock Armor

    // Divine Paladin Warlock Armor

    // Evoker Reverance Warlock Armor

    // Void Seeker Warlock Armor


    public static Collection<RegistryObject<Item>> getDTEItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
