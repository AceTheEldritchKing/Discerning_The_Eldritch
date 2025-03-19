package net.acetheeldritchking.discerning_the_eldritch.utils;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DTETags {
    // Items
    public static final TagKey<Item> RITUAL_FOCUS = ItemTags.create(ResourceLocation.parse(ResourceLocation.fromNamespaceAndPath(DiscerningTheEldritch.MOD_ID, "ritual_focus").toString()));
}
