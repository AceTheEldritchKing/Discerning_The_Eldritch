package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class DTETags {
    /***
     * Items
     */
    // Ritual School Focus
    public static final TagKey<Item> RITUAL_FOCUS = ItemTags.create(ResourceLocation.parse(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "ritual_focus").toString()));

    // Armor Items For Idle
    public static final TagKey<Item> ARMORS_FOR_IDLE = ItemTags.create(ResourceLocation.parse(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "armors_for_idle").toString()));

    // Frozen Weapons
    public static final TagKey<Item> FROZEN_WEAPONS = ItemTags.create(ResourceLocation.parse(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "frozen_weapons").toString()));

    /***
     * Entities
     */
    // Apothic Cultists
    public static final TagKey<EntityType<?>> APOTHIC_ALLIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "apothic_allies"));
}
