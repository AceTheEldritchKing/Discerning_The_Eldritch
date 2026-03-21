package net.acetheeldritchking.discerning_the_eldritch.compat.pastel;

import net.acetheeldritchking.discerning_the_eldritch.DiscerningTheEldritch;
import net.acetheeldritchking.discerning_the_eldritch.blocks.DTEGemstoneBlock;
import net.acetheeldritchking.discerning_the_eldritch.registries.ItemRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PastelCompatBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DiscerningTheEldritch.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DiscerningTheEldritch.MOD_ID);

    //public static final DeferredBlock<Block> AMETHYST_POWDER_BLOCK = register(simple(blockWithItem("amethyst_powder_block", () -> new ColoredFallingBlock(new ColorRGBA(DyeColor.MAGENTA.getFireworkColor()), BlockBehaviour.Properties.ofFullCopy(SAND).mapColor(MapColor.COLOR_MAGENTA)), InkColors.MAGENTA)));
    //public static final DeferredBlock<Block> AMETHYST_CHISELED_CALCITE = register(simple(blockWithItem("amethyst_chiseled_calcite", () -> new Block(BlockBehaviour.Properties.ofFullCopy(PastelBlocks.CALCITE_BRICKS.get()).lightLevel(s -> 5)), InkColors.MAGENTA)));
    //public static final DeferredBlock<Block> AMETHYST_BASALT_LIGHT = registerGemstoneLight("amethyst_basalt_light", AMETHYST_BLOCK, PastelBlocks.POLISHED_BASALT, PastelTextures.BASALT_CAP, InkColors.MAGENTA);
    //public static final DeferredBlock<Block> AMETHYST_CALCITE_LIGHT = registerGemstoneLight("amethyst_calcite_light", AMETHYST_BLOCK, PastelBlocks.POLISHED_CALCITE, PastelTextures.CALCITE_CAP, InkColors.MAGENTA);
    //public static final DeferredBlock<Block> AMETHYST_CHIME = register(translucent(singleton(blockWithItem("amethyst_chime", () -> new GemstoneChimeBlock(chime(AMETHYST_CLUSTER), SoundEvents.AMETHYST_BLOCK_CHIME, ColoredSparkleRisingParticleEffect.MAGENTA), InkColors.MAGENTA), PastelTexturedModels.CHIME)));
    //public static final DeferredBlock<Block> AMETHYST_PYLON = register(pylon(blockWithItem("amethyst_pylon", () -> new PylonBlock(pylon(AMETHYST_BLOCK)), InkColors.MAGENTA)));
    //public static final DeferredBlock<Block> POLISHED_AMETHYST_BLOCK = register(singleton(blockWithItem("polished_amethyst_block", () -> new Block(polishedGemBlock(MapColor.COLOR_MAGENTA, SoundType.AMETHYST)), InkColors.MAGENTA), TexturedModel.TOP_BOTTOM_WITH_WALL));
    public static final DeferredHolder<Block, Block> POLISHED_STARSTONE_BLOCK = registerBlock("polished_starstone_block", () -> new Block(polishedGemBlock(SoundType.AMETHYST)), Rarity.COMMON);

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

    private static BlockBehaviour.Properties polishedGemBlock(SoundType soundGroup) {
        return behaviorProperties(soundGroup, 5.0F, 6.0F);
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
