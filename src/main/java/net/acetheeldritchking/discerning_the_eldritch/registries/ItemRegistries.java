package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockHelmetItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockMaskItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.AscendedArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockHelmetItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.GeckolibEldritchWarlockMaskItem;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.*;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.BlackBookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.GuardianGuidebookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.TempestuousTomeSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.DTEWeaponTiers;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.IceSpearItem;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.YmirSwordItem;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
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
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.PLANAR_SIGHT_SPELL, 4))));

    // God Spear
    public static final DeferredHolder<Item, Item> GOD_SPEAR = ITEMS.register("god_spear", () ->
            new MagicSwordItem(DTEWeaponTiers.GOD_SPEAR, ItemPropertiesHelper.equipment().rarity(Rarity.UNCOMMON).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.GOD_SPEAR)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.DIVINE_SMITE_SPELL, 6))));

    // Ymir
    public static final DeferredHolder<Item, Item> YMIR = ITEMS.register("ymir", YmirSwordItem::new);

    // Ice Spear
    public static final DeferredHolder<Item, Item> ICE_SPEAR = ITEMS.register("ice_spear", IceSpearItem::new);

    // Ritual Dagger
    public static final DeferredHolder<Item, Item> RITUAL_DAGGER = ITEMS.register("ritual_dagger", () ->
            new MagicSwordItem(DTEWeaponTiers.RITUAL_DAGGER, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.RITUAL_DAGGER)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.SACRIFICE_SPELL, 1))));

    // Forlorn Rapier
    public static final DeferredHolder<Item, Item> FORLORN_RAPIER = ITEMS.register("forlorn_rapier", () ->
            new MagicSwordItem(DTEWeaponTiers.FORLORN_RAPIER, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.FORLORN_RAPIER)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.COUNTERSPELL_SPELL, 1))));

    // Lemon Spear

    /***
     * Generic Items
     */

    // Corrupted Cloth - will eventually be a mob drop but not today <3
    public static final DeferredItem<Item> CORRUPTED_CLOTH = ITEMS.register("corrupted_cloth",
            () -> new Item(new Item.Properties()));

    // Eldritch Rune
    public static final DeferredHolder<Item, Item> ELDRITCH_RUNE = ITEMS.register("eldritch_rune", () -> new Item(ItemPropertiesHelper.material()));

    // Eldritch Upgrade Template
    public static final DeferredHolder<Item, Item> ELDRITCH_UPGRADE_TEMPLATE = ITEMS.register("eldritch_upgrade_template",
            () -> new Item(ItemPropertiesHelper.material().stacksTo(16).rarity(Rarity.EPIC)));

    // Ritual Rune
    public static final DeferredHolder<Item, Item> RITUAL_RUNE = ITEMS.register("ritual_rune", () -> new Item(ItemPropertiesHelper.material()));

    /***
     * Upgrade Orbs
     */

    // Eldritch Upgrade Orb
    public static final DeferredHolder<Item, Item> ELDRITCH_UPGRADE_ORB = ITEMS.register("eldritch_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, DTEUpgradeOrbTypeRegistry.ELDRITCH_SPELL_POWER)));

    // Ritual Upgrade Orb
    public static final DeferredHolder<Item, Item> RITUAL_UPGRADE_ORB = ITEMS.register("ritual_upgrade_orb",
            () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, DTEUpgradeOrbTypeRegistry.RITUAL_SPELL_POWER)));

    /***
     * Curios
     */

    // Echo Vibration Ring
    public static final Supplier<CurioBaseItem> ECHO_VIBRATION_RING = ITEMS.register("echo_vibration_ring", EchoVibrationRingCurio::new);

    // King's Effigy
    public static final Supplier<CurioBaseItem> KINGS_EFFIGY = ITEMS.register("kings_effigy", KingsEffigyCurio::new);

    // Amulet of Hidden Treasure
    public static final Supplier<CurioBaseItem> AMULET_OF_SCULK_TREASURE = ITEMS.register("amulet_of_sculk_treasure", AmuletOfSculkTreasureCurio::new);

    // Caster's Mantle
    public static final Supplier<CurioBaseItem> CASTERS_MANTLE = ITEMS.register("casters_mantle", CastersMantleCurio::new);

    // Ironbound Feather
    public static final Supplier<CurioBaseItem> IRONBOUND_FEATHER = ITEMS.register("ironbound_feather", IronboundFeatherCurio::new);

    /***
     * Armor
     */

    // Eldritch Acolyte Accoutrement
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_HOOD = ITEMS.register("eldritch_warlock_hood", () -> new EldritchWarlockArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_ROBES = ITEMS.register("eldritch_warlock_robes", () -> new EldritchWarlockArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_LEGGINGS = ITEMS.register("eldritch_warlock_leggings", () -> new EldritchWarlockArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_GREAVES = ITEMS.register("eldritch_warlock_greaves", () -> new EldritchWarlockArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_MASK = ITEMS.register("eldritch_warlock_mask", () -> new EldritchWarlockMaskItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> ELDRITCH_WARLOCK_HELMET = ITEMS.register("eldritch_warlock_helmet", () -> new EldritchWarlockHelmetItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));

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


    /***
     * Armor
     */
    // Apothic Acolyte
    public static final DeferredItem<Item> ACOLYTE_SPAWN_EGG = ITEMS.register("apothic_acolyte_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.APOTHIC_ACOLYTE, 1973277, 5147788,
                    new Item.Properties()));

    // Apothic Summoner
    public static final DeferredItem<Item> SUMMONER_SPAWN_EGG = ITEMS.register("apothic_summoner_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.APOTHIC_SUMMONER, 1973277, 9324137,
                    new Item.Properties()));

    // Apothic Crusader
    public static final DeferredItem<Item> CRUSADER_SPAWN_EGG = ITEMS.register("apothic_crusader_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.APOTHIC_CRUSADER, 1973277, 12556098,
                    new Item.Properties()));


    // GECKOLIB ARMOR VERSIONS FOR NPCS/NON-PLAYER ENTITIES - THESE ARE NOT OBTAINABLE)

    // Eldritch Set
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_HOOD = ITEMS.register("gecko_eldritch_warlock_hood", () -> new GeckolibEldritchWarlockArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_ROBES = ITEMS.register("gecko_eldritch_warlock_robes", () -> new GeckolibEldritchWarlockArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_LEGGINGS = ITEMS.register("gecko_eldritch_warlock_leggings", () -> new GeckolibEldritchWarlockArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_GREAVES = ITEMS.register("gecko_eldritch_warlock_greaves", () -> new GeckolibEldritchWarlockArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_MASK = ITEMS.register("gecko_eldritch_warlock_mask", () -> new GeckolibEldritchWarlockMaskItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_ELDRITCH_WARLOCK_HELMET = ITEMS.register("gecko_eldritch_warlock_helmet", () -> new GeckolibEldritchWarlockHelmetItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));

    // Ascended Set
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_HOOD = ITEMS.register("ascended_one_hood", () -> new AscendedArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_ROBES = ITEMS.register("ascended_one_robes", () -> new AscendedArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_LEGGINGS = ITEMS.register("ascended_one_leggings", () -> new AscendedArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_GREAVES = ITEMS.register("ascended_one_greaves", () -> new AscendedArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));


    public static Collection<DeferredHolder<Item, ? extends Item>> getDTEItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
