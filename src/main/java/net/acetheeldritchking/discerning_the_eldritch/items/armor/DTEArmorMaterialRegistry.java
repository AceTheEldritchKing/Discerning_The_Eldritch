package net.acetheeldritchking.discerning_the_eldritch.items.armor;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class DTEArmorMaterialRegistry {
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, DiscerningTheEldritch.MOD_ID);

    // Eldritch Warlock
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> ELDRITCH_WARLOCK = register("eldritch_mage_armor",
            warlockArmorMap(),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ItemRegistries.CORRUPTED_CLOTH.get()),
            3,
            0.1F);

    // Mage Hunter

    // Pryomancer Warlock Armor

    // Cryomancer Warlock Armor

    // Electromancer Warlock Armor

    // Eternal Druid Warlock Armor

    // Sanguine Cultist Warlock Armor

    // Divine Paladin Warlock Armor

    // Evoker Reverance Warlock Armor

    // Starvoid Warlock Armor
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> STARVOID_ARMOR = register("starvoid_armor",
            warlockArmorMap(),
            25,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(ItemRegistries.STARMETAL_INGOT.get()),
            3,
            0.1F);

    // Crimson Stag Armor
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> CRIMSON_STAG = register("crimson_stag_armor",
            warlockArmorMap(),
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(ItemRegistry.MAGIC_CLOTH.get()),
            3,
            0.1F);

    // Sculk Apostle Wardress Armor
    public static DeferredHolder<ArmorMaterial, ArmorMaterial> SCULK_APOSTLE_ARMOR = register("sculk_apostle_armor",
            warlockArmorMap(),
            25,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(ItemRegistries.CORRUPTED_CLOTH.get()),
            3,
            0.1F);

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            float toughness,
            float knockbackResistance
    )
    {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(DiscerningTheEldritch.id(name)));
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient, list, toughness, knockbackResistance));
    }

    public static EnumMap<ArmorItem.Type, Integer> makeArmorMap(int helmet, int chestplate, int leggings, int boots)
    {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), (typeIntegerEnumMap) -> {
            typeIntegerEnumMap.put(ArmorItem.Type.HELMET, helmet);
            typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, chestplate);
            typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, leggings);
            typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, boots);
        });
    }

    public static EnumMap<ArmorItem.Type, Integer> warlockArmorMap()
    {
        return makeArmorMap(4, 9, 7, 4);
    }

    public static EnumMap<ArmorItem.Type, Integer> eldritchKingArmorMap()
    {
        return makeArmorMap(6, 11, 9, 6);
    }

    public static void register(IEventBus eventBus)
    {
        ARMOR_MATERIALS.register(eventBus);
    }
}
