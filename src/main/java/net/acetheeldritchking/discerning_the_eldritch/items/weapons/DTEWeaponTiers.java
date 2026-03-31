package net.acetheeldritchking.discerning_the_eldritch.items.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.acetheeldritchking.discerning_the_eldritch.registries.DTEAttributeRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class DTEWeaponTiers implements Tier, IronsWeaponTier {
    // Greatsword Of The Depths
    public static DTEWeaponTiers DEEP_GREATSWORD = new DTEWeaponTiers(1680, 7.5F, -3.0F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // God Spear
    public static DTEWeaponTiers GOD_SPEAR = new DTEWeaponTiers(1560, 8.5F, -3.2F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Ender Cleaver
    public static DTEWeaponTiers ENDER_CLEAVER = new DTEWeaponTiers(1600, 6.5F, -2.5F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Glaive of The Gods

    // Hammer of Hephaestus

    // Bane of The King

    // Skull Staff

    // Ice Spear
    public static DTEWeaponTiers ICE_SPEAR = new DTEWeaponTiers(1500, 6F, -2.8F, 8, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Ymir
    public static DTEWeaponTiers YMIR = new DTEWeaponTiers(2670, 15.5F, -3.1F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MANA_STEAL, 0.10D, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.MANA_REND, 0.25D, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.1D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Ritual Dagger
    public static DTEWeaponTiers RITUAL_DAGGER = new DTEWeaponTiers(900, 2, -2.6F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Forlorn Rapier
    public static DTEWeaponTiers FORLORN_RAPIER = new DTEWeaponTiers(1061, 5, -1.5F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Broken Legend's Blade
    public static DTEWeaponTiers BROKEN_LEGENDS_SWORD = new DTEWeaponTiers(500, 6.5F, -2.8F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );

    // Forsaken Flamberge
    public static DTEWeaponTiers FORSAKEN_FLAMBERGE = new DTEWeaponTiers(2031, 10F, -2.7F, 6, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MANA_STEAL, 0.05D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Snowgrave
    public static DTEWeaponTiers SNOWGRAVE = new DTEWeaponTiers(1785, 9.5F, -3.2F, 10, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    // Soul Fire Scythe
    public static DTEWeaponTiers SOUL_FIRE_SCYTHE = new DTEWeaponTiers(2061, 12F, -2.7F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.FIRE_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.05D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Cataclysm (Dormant)
    public static DTEWeaponTiers CATACLYSM = new DTEWeaponTiers(1281, 10.0F, -2.7F, 9, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.GOLIATH_SLAYER, 0.05, AttributeModifier.Operation.ADD_VALUE)
    );
    // Cataclysm (Awakened)
    public static DTEWeaponTiers CATACLYSM_AWAKENED = new DTEWeaponTiers(2370, 13.0F, -2.7F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.GOLIATH_SLAYER, 0.1, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.05, AttributeModifier.Operation.ADD_VALUE)
    );

    // Devourer (Dormant)
    public static DTEWeaponTiers DEVOURER = new DTEWeaponTiers(1281, 12.0F, -3.15F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.HUNGER_STEAL, 1, AttributeModifier.Operation.ADD_VALUE)
    );
    // Devourer (Awakened)
    public static DTEWeaponTiers DEVOURER_AWAKENED = new DTEWeaponTiers(2370, 14.0F, -3.15F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.HUNGER_STEAL, 2, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.05, AttributeModifier.Operation.ADD_VALUE)
    );

    // Mourning Star (Dormant)
    public static DTEWeaponTiers MOURNING_STAR = new DTEWeaponTiers(1281, 10.5F, -2.9F, 9, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MANA_REND, 0.05, AttributeModifier.Operation.ADD_VALUE)
    );
    // Mourning Star (Awakened)
    public static DTEWeaponTiers MOURNING_STAR_AWAKENED = new DTEWeaponTiers(2370, 13.5F, -2.9F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.RITUAL_MAGIC_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MANA_REND, 0.1, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.SPELL_RES_PENETRATION, 0.05, AttributeModifier.Operation.ADD_VALUE)
    );

    // Starmetal Scythe
    public static DTEWeaponTiers STARMETAL_SCYTHE = new DTEWeaponTiers(2261, 13F, -2.7F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MAGIC_PROJECTILE_DAMAGE, 0.10D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Starmetal Scythe
    public static DTEWeaponTiers STARMETAL_ODACHI = new DTEWeaponTiers(2061, 9.5F, -2.5F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.ENDER_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.LIFE_RECOVERY, 0.05D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Void Splitter
    public static DTEWeaponTiers VOIDSPLITTER = new DTEWeaponTiers(2061, 13.5F, -2.5F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MAGIC_DAMAGE_CRIT_CHANCE, 0.15, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.LIFE_RECOVERY, 0.10D, AttributeModifier.Operation.ADD_VALUE)
    );

    // Staff of The Spectre
    public static DTEWeaponTiers STAFF_OF_THE_SPECTRE = new DTEWeaponTiers(2061, 14.5F, -2.8F, 15, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of(ItemRegistry.MITHRIL_INGOT.get()),
            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new AttributeContainer(ASAttributeRegistry.MANA_REND, 0.25D, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.MAGIC_DAMAGE_CRIT_CHANCE, 0.25D, AttributeModifier.Operation.ADD_VALUE),
            new AttributeContainer(ASAttributeRegistry.MAGIC_PROJECTILE_DAMAGE, 0.25D, AttributeModifier.Operation.ADD_VALUE)
    );


    //private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final TagKey<Block> incorrectBlocksForDrops;
    private final Supplier<Ingredient> repairIngredient;
    private final AttributeContainer[] attributeContainers;

    private DTEWeaponTiers(int uses, float damage, float speed, int enchantmentValue, TagKey<Block> incorrectBlocksForDrops, Supplier<Ingredient> repairIngredient, AttributeContainer... attributes) {
        //this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.repairIngredient = repairIngredient;
        this.attributeContainers = attributes;
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributeContainers;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
