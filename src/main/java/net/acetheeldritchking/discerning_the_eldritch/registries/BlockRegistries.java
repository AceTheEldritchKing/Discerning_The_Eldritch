package net.acetheeldritchking.discerning_the_eldritch.registries;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.blocks.DTEBuddingGemstoneBlock;
import net.acetheeldritchking.discerning_the_eldritch.blocks.DTEClusterBlock;
import net.acetheeldritchking.discerning_the_eldritch.blocks.DTEGemstoneBlock;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockRegistries {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DiscerningTheEldritch.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DiscerningTheEldritch.MOD_ID);

    // Meteorstone Blocks
    public static final DeferredHolder<Block, Block> METEORSTONE_BLOCK = registerBlock("meteorstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)), Rarity.COMMON);
    public static final DeferredHolder<Block, Block> METEORSTONE_BRICKS = registerBlock("meteorstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)), Rarity.COMMON);
    public static final DeferredHolder<Block, Block> METEORSTONE_TILES = registerBlock("meteorstone_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES)), Rarity.COMMON);

    // Starstone Geodes
    public static final DeferredHolder<Block, Block> STARSTONE_ORE = registerBlock("starstone_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_DIAMOND_ORE)), Rarity.COMMON);
    public static final DeferredHolder<Block, Block> STARSTONE_BLOCK = registerBlock("starstone_block", () -> new DTEGemstoneBlock(gemstoneBlockProperties(SoundType.AMETHYST), SoundEvents.AMETHYST_BLOCK_HIT, SoundEvents.AMETHYST_BLOCK_CHIME), Rarity.COMMON);
    public static final DeferredHolder<Block, Block> SMALL_STARSTONE_BUD = registerBlock("small_starstone_bud", () -> new DTEClusterBlock
                    (
                            gemstoneBlockProperties(SoundType.AMETHYST),
                            DTEClusterBlock.GrowthStage.SMALL
                    ),
            Rarity.COMMON);
    public static final DeferredHolder<Block, Block> MEDIUM_STARSTONE_BUD = registerBlock("medium_starstone_bud", () -> new DTEClusterBlock
                    (
                            gemstoneBlockProperties(SoundType.AMETHYST),
                            DTEClusterBlock.GrowthStage.MEDIUM
                    ),
            Rarity.COMMON);
    public static final DeferredHolder<Block, Block> LARGE_STARSTONE_BUD = registerBlock("large_starstone_bud", () -> new DTEClusterBlock
                    (
                            gemstoneBlockProperties(SoundType.AMETHYST),
                            DTEClusterBlock.GrowthStage.LARGE
                    ),
            Rarity.COMMON);
    public static final DeferredHolder<Block, Block> STARSTONE_CLUSTER = registerBlock("starstone_cluster", () -> new DTEClusterBlock
                    (
                            gemstoneBlockProperties(SoundType.AMETHYST),
                            DTEClusterBlock.GrowthStage.CLUSTER
                    ),
            Rarity.COMMON);
    public static final DeferredHolder<Block, Block> BUDDING_STARSTONE_BLOCK = registerBlock("budding_starstone", () -> new DTEBuddingGemstoneBlock
                    (
                            gemstoneBlockProperties(SoundType.AMETHYST).pushReaction(PushReaction.DESTROY).randomTicks(),
                            SoundEvents.AMETHYST_BLOCK_HIT,
                            SoundEvents.AMETHYST_BLOCK_CHIME,
                            BlockRegistries.SMALL_STARSTONE_BUD.get(),
                            BlockRegistries.MEDIUM_STARSTONE_BUD.get(),
                            BlockRegistries.LARGE_STARSTONE_BUD.get(),
                            BlockRegistries.STARSTONE_CLUSTER.get()
                    ),
            Rarity.COMMON);

    // Block helpers
    // I referenced the gemstone stuff from Pastel
    private static BlockBehaviour.Properties behaviorProperties(SoundType soundType, float strength, float resistance)
    {
        return BlockBehaviour.Properties.of().sound(soundType).strength(strength).explosionResistance(resistance);
    }

    private static BlockBehaviour.Properties behaviorProperties(SoundType soundType, float strength)
    {
        return BlockBehaviour.Properties.of().sound(soundType).strength(strength);
    }

    private static BlockBehaviour.Properties gemstoneBlockProperties(SoundType soundType)
    {
        return behaviorProperties(soundType, 1.5F).requiresCorrectToolForDrops();
    }

    private static BlockBehaviour.Properties gemstoneGemProperties(SoundType soundType, int brightness)
    {
        return behaviorProperties(soundType, 1.5F).forceSolidOn().noOcclusion().lightLevel((state) -> brightness).pushReaction(PushReaction.DESTROY);
    }

    private static <T extends Block> DeferredHolder<Block, Block> registerBlock(String name, Supplier<T> block, Rarity rarity)
    {
        DeferredHolder<Block, Block> deferredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, deferredBlock, rarity);
        return deferredBlock;
    }

    private static <T extends Block> DeferredHolder<Block, Block> registerBlock(String name, Supplier<T> block, Item.Properties itemProperties)
    {
        DeferredHolder<Block, Block> deferredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, deferredBlock, itemProperties);
        return deferredBlock;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<T, T> block, Rarity rarity)
    {
        ItemRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredHolder<T, T> block, Item.Properties itemProperties)
    {
        ItemRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
