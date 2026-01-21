package net.acetheeldritchking.discerning_the_eldritch.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.SpellSlotUpgradeItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.acetheeldritchking.aces_spell_utils.utils.ASRarities;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.CrimsonStagArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockArmorItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockHelmetItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.EldritchWarlockMaskItem;
import net.acetheeldritchking.discerning_the_eldritch.items.armor.Geckolib.*;
import net.acetheeldritchking.discerning_the_eldritch.items.curios.*;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.AscendedOneLootbag;
import net.acetheeldritchking.discerning_the_eldritch.items.custom.FadingSculkLantern;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.BlackBookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.DiaryOfDecaySpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.GuardianGuidebookSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.spellbooks.StormWeaverTomeSpellbook;
import net.acetheeldritchking.discerning_the_eldritch.items.staffs.BrokenLegendsStaff;
import net.acetheeldritchking.discerning_the_eldritch.items.staffs.DTEStaffTier;
import net.acetheeldritchking.discerning_the_eldritch.items.staffs.StaffOfVehemenceStaffItem;
import net.acetheeldritchking.discerning_the_eldritch.items.weapons.*;
import net.acetheeldritchking.discerning_the_eldritch.utils.DTERarities;
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
            new SpellBook(13, ItemPropertiesHelper.equipment().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue()).fireResistant().stacksTo(1))
                    .withSpellbookAttributes
                            (
                                new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .20F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                new AttributeContainer(AttributeRegistry.MAX_MANA, 300, AttributeModifier.Operation.ADD_VALUE)
                            )
    );

    // Tempestuous Time
    public static final DeferredHolder<Item, Item> TEMPESTUOUS_TOME = ITEMS.register("tempestuous_tome", StormWeaverTomeSpellbook::new);

    // Frozen Folio
    public static final DeferredHolder<Item, Item> FROZEN_FOLIO = ITEMS.register("frozen_folio", () ->
            new SpellBook(8).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, .10F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
            ));

    // Diary of Decay
    public static final DeferredHolder<Item, Item> DIARY_OF_DECAY  = ITEMS.register("diary_of_decay", DiaryOfDecaySpellbook::new);

    // Guardian's Gaze
    public static final DeferredHolder<Item, Item> GUARDIANS_GAZE = ITEMS.register("guardian_guidebook", GuardianGuidebookSpellbook::new);


    /***
     * Staffs and Gauntlets
     */

    // Staff of Vehemence
    public static final DeferredHolder<Item, Item> STAFF_OF_VEHEMENCE = ITEMS.register("staff_of_eldritch", StaffOfVehemenceStaffItem::new);

    // Hand of Apocrypha

    // Staff of Ascension
    public static final DeferredHolder<Item, Item> STAFF_OF_ASCENSION = ITEMS.register("staff_of_ascension", () -> new StaffItem(ItemPropertiesHelper.equipment(1).fireResistant().rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(DTEStaffTier.STAFF_OF_ASCENSION))));

    // Broken Legend's Staff
    public static final DeferredHolder<Item, Item> BROKEN_LEGENDS_STAFF = ITEMS.register("broken_legends_staff", BrokenLegendsStaff::new);


    /***
     * Weapons
     */

    // Deep Greatsword
    public static final DeferredHolder<Item, Item> DEEP_GREATSWORD = ITEMS.register("deep_greatsword", () ->
            new MagicSwordItem(DTEWeaponTiers.DEEP_GREATSWORD, ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.DEEP_GREATSWORD)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.PLANAR_SIGHT_SPELL, 4))));

    // God Spear
    public static final DeferredHolder<Item, Item> GOD_SPEAR = ITEMS.register("god_spear", () ->
            new MagicSwordItem(DTEWeaponTiers.GOD_SPEAR, ItemPropertiesHelper.equipment(1).rarity(Rarity.UNCOMMON).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.GOD_SPEAR)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.DIVINE_SMITE_SPELL, 6))));

    // Ymir
    public static final DeferredHolder<Item, Item> YMIR = ITEMS.register("ymir", YmirSwordItem::new);

    // Ice Spear
    public static final DeferredHolder<Item, Item> ICE_SPEAR = ITEMS.register("ice_spear", IceSpearItem::new);

    // Ritual Dagger
    public static final DeferredHolder<Item, Item> RITUAL_DAGGER = ITEMS.register("ritual_dagger", () ->
            new MagicSwordItem(DTEWeaponTiers.RITUAL_DAGGER, ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.RITUAL_DAGGER)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.SACRIFICE_SPELL, 1))));

    // Forlorn Rapier
    public static final DeferredHolder<Item, Item> FORLORN_RAPIER = ITEMS.register("forlorn_rapier", () ->
            new MagicSwordItem(DTEWeaponTiers.FORLORN_RAPIER, ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.FORLORN_RAPIER)),
                    SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.COUNTERSPELL_SPELL, 1))));

    // Lemon Spear

    // Broken Legend's Blade
    public static final DeferredHolder<Item, Item> BROKEN_LEGENDS_BLADE = ITEMS.register("broken_legends_blade", BrokenLegendsBlade::new);

    // Forsaken Flamberge
    public static final DeferredHolder<Item, Item> FORSAKEN_FLAMBERGE = ITEMS.register("forsaken_flamberge", () ->
            new ExtendedSwordItem(DTEWeaponTiers.FORSAKEN_FLAMBERGE, ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE).attributes(ExtendedSwordItem.createAttributes(DTEWeaponTiers.FORSAKEN_FLAMBERGE))));

    // Snowgrave
    public static final DeferredHolder<Item, Item> SNOWGRAVE = ITEMS.register("snowgrave", TheSnowgraveItem::new);

    // Soul Fire Scythe
    public static final DeferredHolder<Item, Item> SOUL_FIRE_SCYTHE = ITEMS.register("soul_fire_scythe", SoulFireScytheItem::new);

    // Cataclysm (Dormant)
    public static final DeferredHolder<Item, Item> CATACLYSM = ITEMS.register("cataclysm", CataclysmBladeItem::new);
    // Cataclysm (Awakened)
    public static final DeferredHolder<Item, Item> CATACLYSM_AWAKENED = ITEMS.register("cataclysm_awakened", CataclysmBladeAwakenedItem::new);

    // Devourer (Dormant)
    public static final DeferredHolder<Item, Item> DEVOURER = ITEMS.register("devourer", DevourerAxeItem::new);
    // Devourer (Awakened)
    public static final DeferredHolder<Item, Item> DEVOURER_AWAKENED = ITEMS.register("devourer_awakened", DevourerAxeAwakenedItem::new);

    // Mourning Star (Dormant)
    public static final DeferredHolder<Item, Item> MOURNING_STAR = ITEMS.register("mourning_star", MourningStarMaceItem::new);
    // Mourning Star (Awakened)
    public static final DeferredHolder<Item, Item> MOURNING_STAR_AWAKENED = ITEMS.register("mourning_star_awakened", MourningStarMaceAwakenedItem::new);

    // Starmetal Scythe
    public static final DeferredHolder<Item, Item> STARMETAL_SCYTHE = ITEMS.register("starmetal_scythe", StarmetalScytheItem::new);

    // Staff of The Spectre
    public static final DeferredHolder<Item, Item> STAFF_OF_THE_SPECTRE = ITEMS.register("staff_of_the_spectre", StaffOfTheSpectre::new);


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

    // Apothic Key
    public static final DeferredHolder<Item, Item> APOTHIC_KEY = ITEMS.register("apothic_key", () -> new Item(ItemPropertiesHelper.material()));

    // Ascended Key
    public static final DeferredHolder<Item, Item> ASCENDED_KEY = ITEMS.register("ascended_key", () -> new Item(ItemPropertiesHelper.material()));

    // Ritual Key
    public static final DeferredHolder<Item, Item> RITUAL_KEY = ITEMS.register("ritual_key", () -> new Item(ItemPropertiesHelper.material()));

    // Eldritch Soul Shard
    public static final DeferredHolder<Item, Item> ELDRITCH_SOUL_SHARD = ITEMS.register("eldritch_soul_shard", () -> new Item(ItemPropertiesHelper.material().rarity(DTERarities.APOTHIC_RARITY_PROXY.getValue())));

    // Fading Sculk Lantern
    public static final DeferredHolder<Item, Item> FADING_SCULK_LANTERN = ITEMS.register("fading_sculk_lantern", FadingSculkLantern::new);

    // Shard of Malice
    public static final DeferredHolder<Item, Item> SHARD_OF_MALICE = ITEMS.register("shard_of_malice",
            () -> new Item(ItemPropertiesHelper.material().stacksTo(16).rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue())));

    // Bottle of Liquid Malice
    public static final DeferredHolder<Item, Item> BOTTLE_OF_LIQUID_MALICE = ITEMS.register("bottle_of_liquid_malice",
            () -> new Item(ItemPropertiesHelper.material().stacksTo(16).rarity(ASRarities.ACCURSED_RARITY_PROXY.getValue())));

    // Forbidden Spell Improvement
    public static final DeferredHolder<Item, Item> FORBIDDEN_SPELL_IMPROVEMENT = ITEMS.register("forbidden_spell_improvement",
            () -> new SpellSlotUpgradeItem(15));

    // Soul Ember
    public static final DeferredHolder<Item, Item> SOUL_EMBER = ITEMS.register("soul_ember",
            () -> new Item(ItemPropertiesHelper.material().rarity(ASRarities.AQUATIC_RARITY_PROXY.getValue())));

    // Ascended One Lootbag
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_LOOTBAG = ITEMS.register("ascended_one_lootbag", AscendedOneLootbag::new);

    // Starmetal Ingot
    public static final DeferredHolder<Item, Item> STARMETAL_INGOT = ITEMS.register("starmetal_ingot",
            () -> new Item(ItemPropertiesHelper.material().fireResistant().rarity(ASRarities.ARID_RARITY_PROXY.getValue())));


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

    // Ironbound Feather - Trial Chamber loot
    public static final Supplier<CurioBaseItem> IRONBOUND_FEATHER = ITEMS.register("ironbound_feather", IronboundFeatherCurio::new);

    // Pyrium Sheath
    public static final Supplier<CurioBaseItem> PYRIUM_SHEATH = ITEMS.register("pyrium_sheath", PyriumSheathCurio::new);

    // Frostbourne Sheath
    public static final Supplier<CurioBaseItem> FROSTBOURNE_SHEATH = ITEMS.register("frostbourne_sheath", FrostbourneSheathCurio::new);

    // Emblem of The Rift Ripper
    public static final Supplier<CurioBaseItem> RIFT_RIPPER_EMBLEM = ITEMS.register("rift_ripper_emblem", RiftRipperEmblemCurio::new);

    // Razor Sheath
    public static final Supplier<CurioBaseItem> RAZOR_SHEATH = ITEMS.register("razor_sheath", RazorSheathCurio::new);

    // Starstruck Sheath
    public static final Supplier<CurioBaseItem> STARSTRUCK_SHEATH = ITEMS.register("starstruck_sheath", StarstruckSheathCurio::new);


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
    public static final DeferredHolder<Item, Item> CRIMSON_STAG_ANTLERS = ITEMS.register("crimson_stag_antlers", () -> new CrimsonStagArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> CRIMSON_STAG_ROBES = ITEMS.register("crimson_stag_robes", () -> new CrimsonStagArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> CRIMSON_STAG_LEGGINGS = ITEMS.register("crimson_stag_leggings", () -> new CrimsonStagArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> CRIMSON_STAG_BOOTS = ITEMS.register("crimson_stag_boots", () -> new CrimsonStagArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));


    /***
     * Spawn Eggs
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

    // Ascended Cultist
    public static final DeferredItem<Item> ASCENDED_CULTIST_SPAWN_EGG = ITEMS.register("ascended_cultist_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.ASCENDED_ONE_CULTIST, 1973277, 10554050,
                    new Item.Properties()));

    // Blood Captain
    public static final DeferredItem<Item> BLOOD_CULTIST_CAPTAIN_SPAWN_EGG = ITEMS.register("blood_cultist_captain_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.BLOOD_CULTIST_CAPTAIN, 4203295, 10357001,
                    new Item.Properties()));

    // Blood Mage
    public static final DeferredItem<Item> BLOOD_CULTIST_MAGE_SPAWN_EGG = ITEMS.register("blood_cultist_mage_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.BLOOD_CULTIST_MAGE, 4203295, 16209219,
                    new Item.Properties()));

    // Blood Witch
    public static final DeferredItem<Item> BLOOD_CULTIST_WITCH_SPAWN_EGG = ITEMS.register("blood_cultist_witch_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.BLOOD_CULTIST_WITCH, 4203295, 10231815,
                    new Item.Properties()));

    // Blood Matriarch
    public static final DeferredItem<Item> BLOOD_MATRIARCH_SPAWN_EGG = ITEMS.register("blood_matriarch_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.BLOOD_MATRIARCH, 4203295, 16066367,
                    new Item.Properties()));

    // Electromancer
    public static final DeferredItem<Item> ELECTROMANCER_SPAWN_EGG = ITEMS.register("electromancer_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.ELECTROMANCER_MAGE, 16097366, 7729392,
                    new Item.Properties()));

    // Apostle Of Sculk
    public static final DeferredItem<Item> APOSTLE_OF_SCULK_SPAWN_EGG = ITEMS.register("apostle_of_sculk_spawn_egg",
            () -> new DeferredSpawnEggItem(DTEEntityRegistry.APOSTLE_OF_SCULK, 2640476, 5892331,
                    new Item.Properties()));


    /***
     * Gecko Armor
     */
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
    public static final DeferredHolder<Item, Item> CAPELESS_ASCENDED_ONE_ROBES = ITEMS.register("capeless_ascended_one_robes", () -> new CapelessAscendedArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_LEGGINGS = ITEMS.register("ascended_one_leggings", () -> new AscendedArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> ASCENDED_ONE_GREAVES = ITEMS.register("ascended_one_greaves", () -> new AscendedArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));

    // Crimson Stag
    public static final DeferredHolder<Item, Item> GECKOLIB_CRIMSON_STAG_ANTLERS = ITEMS.register("gecko_crimson_stag_antlers", () -> new GeckolibCrimsonStagArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_CRIMSON_STAG_ROBES = ITEMS.register("gecko_crimson_stag_robes", () -> new GeckolibCrimsonStagArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_CRIMSON_STAG_LEGGINGS = ITEMS.register("gecko_crimson_stag_leggings", () -> new GeckolibCrimsonStagArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> GECKOLIB_CRIMSON_STAG_BOOTS = ITEMS.register("gecko_crimson_stag_boots", () -> new GeckolibCrimsonStagArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));

    // Sculk Apostle Set
    public static final DeferredHolder<Item, Item> SCULK_APOSTLE_HOOD = ITEMS.register("sculk_apostle_hood", () -> new SculkApostleArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredHolder<Item, Item> SCULK_APOSTLE_ROBES = ITEMS.register("sculk_apostle_robes", () -> new SculkApostleArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredHolder<Item, Item> SCULK_APOSTLE_LEGGINGS = ITEMS.register("sculk_apostle_leggings", () -> new SculkApostleArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredHolder<Item, Item> SCULK_APOSTLE_GREAVES = ITEMS.register("sculk_apostle_greaves", () -> new SculkApostleArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(40))));


    public static Collection<DeferredHolder<Item, ? extends Item>> getDTEItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
