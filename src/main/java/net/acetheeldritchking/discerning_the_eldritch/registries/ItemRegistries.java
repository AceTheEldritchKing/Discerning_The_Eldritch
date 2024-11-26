package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.EchoVibrationRing;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.KingsEffigyCurio;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.BlackBookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.GuardianGuidebookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.TempestuousTomeSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.DTEWeaponTiers;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.IceSpearItem;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.YmirSwordItem;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
    public static final DeferredHolder<Item, Item> BLACK_BOOK_SPELLBOOK = ITEMS.register("black_book_spellbook", BlackBookSpellbook::new);

    // The Apocrypha
    public static final DeferredHolder<Item, Item> THE_APOCRYPHA_SPELLBOOK  = ITEMS.register("the_apocrypha_spellbook", () ->
            new SpellBook(13).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .20F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 300, AttributeModifier.Operation.ADD_VALUE)
            ));

    // Tempestuous Time
    public static final DeferredHolder<Item, Item> TEMPESTUOUS_TOME = ITEMS.register("tempestuous_tome", TempestuousTomeSpellbook::new);

    // Frozen Folio
    public static final DeferredHolder<Item, Item> FROZEN_FOLIO = ITEMS.register("frozen_folio", () ->
            new SpellBook(8).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, .10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
            ));

    // Diary of Decay
    public static final DeferredHolder<Item, Item> DIARY_OF_DECAY  = ITEMS.register("diary_of_decay", () ->
            new SpellBook(8).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, .10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
            ));

    // Guardian's Gaze
    public static final DeferredHolder<Item, Item> GUARDIANS_GAZE = ITEMS.register("guardian_guidebook", GuardianGuidebookSpellbook::new);

    /***
     * Staffs and Gauntlets
     */

    // Hand of Apocrypha

    /***
     * Weapons
     */

    // Deep Greatsword
    public static final DeferredHolder<Item, Item> DEEP_GREATSWORD = ITEMS.register("deep_greatsword", () ->
            new MagicSwordItem(DTEWeaponTiers.DEEP_GREATSWORD, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.DEEP_GREATSWORD)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.PLANAR_SIGHT_SPELL, 6))));

    // God Spear
    public static final DeferredHolder<Item, Item> GOD_SPEAR = ITEMS.register("god_spear", () ->
            new MagicSwordItem(DTEWeaponTiers.GOD_SPEAR, ItemPropertiesHelper.equipment().rarity(Rarity.UNCOMMON).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.GOD_SPEAR)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.DIVINE_SMITE_SPELL, 6))));

    // Ymir
    public static final DeferredHolder<Item, Item> YMIR = ITEMS.register("ymir", YmirSwordItem::new);

    // Ice Spear
    public static final DeferredHolder<Item, Item> ICE_SPEAR = ITEMS.register("ice_spear", IceSpearItem::new);

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

    // King's Effigy
    public static final Supplier<CurioBaseItem> KINGS_EFFIGY = ITEMS.register("kings_effigy", KingsEffigyCurio::new);

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
