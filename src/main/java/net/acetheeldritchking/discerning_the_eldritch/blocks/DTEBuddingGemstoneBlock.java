package net.acetheeldritchking.discerning_the_eldritch.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class DTEBuddingGemstoneBlock extends DTEGemstoneBlock {
    public static final MapCodec<DTEBuddingGemstoneBlock> CODEC =
            RecordCodecBuilder.mapCodec(i -> i.group(
                            propertiesCodec(),
                            SoundEvent.DIRECT_CODEC.fieldOf("hit_sound_event")
                                    .forGetter(b -> b.hitSoundEvent),
                            SoundEvent.DIRECT_CODEC.fieldOf("chime_sound_event")
                                    .forGetter(b -> b.chimeSoundEvent),
                            Block.CODEC.fieldOf("small_bud")
                                    .forGetter(b -> b.smallBud),
                            Block.CODEC.fieldOf("medium_bud")
                                    .forGetter(b -> b.mediumBud),
                            Block.CODEC.fieldOf("large_bud")
                                    .forGetter(b -> b.largeBud),
                            Block.CODEC.fieldOf("cluster")
                                    .forGetter(b -> b.cluster)
                    )
                    .apply(
                            i, DTEBuddingGemstoneBlock::new
                    ));

    private final SoundEvent hitSoundEvent;
    private final SoundEvent chimeSoundEvent;
    private static final Direction[] DIRECTIONS = Direction.values();
    private final Block smallBud;
    private final Block mediumBud;
    private final Block largeBud;
    private final Block cluster;

    public DTEBuddingGemstoneBlock(Properties properties, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent, Block smallBud, Block mediumBud, Block largeBud, Block cluster) {
        super(properties, hitSoundEvent, chimeSoundEvent);
        this.chimeSoundEvent = chimeSoundEvent;
        this.hitSoundEvent = hitSoundEvent;

        this.smallBud = smallBud;
        this.mediumBud = mediumBud;
        this.largeBud = largeBud;
        this.cluster = cluster;
    }

    @Override
    public MapCodec<? extends DTEBuddingGemstoneBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0)
        {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;

            if (BuddingAmethystBlock.canClusterGrowAtState(blockstate))
            {
                block = smallBud;
            } else if (blockstate.is(smallBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = mediumBud;
            } else if (blockstate.is(mediumBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = largeBud;
            } else if (blockstate.is(largeBud) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = cluster;
            }

            if (block != null)
            {
                level.setBlockAndUpdate(
                        blockpos, block.defaultBlockState()
                                .setValue(AmethystClusterBlock.FACING, direction)
                                .setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER)
                );
            }
        }
    }
}
