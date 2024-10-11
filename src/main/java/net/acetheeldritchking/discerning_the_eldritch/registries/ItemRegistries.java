package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.EchoVibrationRing;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class ItemRegistries {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DiscerningTheEldritch.MOD_ID);

    /***
     * Spellbooks
     */

    // Black Book
    public static final DeferredHolder<Item, Item> BLACK_BOOK_SPELLBOOK = ITEMS.register("black_book_spellbook", () ->
            new SpellBook(12).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
            ));

    // The Apocrypha
    public static final DeferredHolder<Item, Item> THE_APOCRYPHA_SPELLBOOK  = ITEMS.register("the_apocrypha_spellbook", () ->
            new SpellBook(13).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .20F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 300, AttributeModifier.Operation.ADD_VALUE)
            ));

    /***
     * Staffs and Gauntlets
     */

    // Hand of Apocrypha

    /***
     * Generic Items
     */

    // Corrupted Cloth - will eventually be a mob drop but not today <3
    public static final DeferredItem<Item> CORRUPTED_CLOTH = ITEMS.register("corrupted_cloth",
            () -> new Item(new Item.Properties()));

    /***
     * Curios
     */

    // Echo Vibration Ring
    public static final Supplier<CurioBaseItem> ECHO_VIBRATION_RING = ITEMS.register("echo_vibration_ring", EchoVibrationRing::new);

    /***
     * Armor
     */

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

    // Crimson Stag Armor (Blood & Nature set)


    public static Collection<DeferredHolder<Item, ? extends Item>> getDTEItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
